# Courier Tracker

## Назначение и стек

Локальное Android-приложение для учёта сервисов/счетов, рабочих слотов,
категорий, доходов и расходов; главная показывает операции и баланс.
Один Gradle-модуль `:app`, пакет `com.example.couriertracker`.

- Kotlin, Jetpack Compose, Material 3, Material Icons Extended; XML для ресурсов и manifest.
- AndroidX Lifecycle ViewModel, корутины, Flow/StateFlow, `collectAsStateWithLifecycle`.
- Navigation 3, сериализуемые `NavKey` через kotlinx.serialization.
- Room **3** (`androidx.room3`, не Room 2), KSP; Preferences DataStore.
- Gradle Kotlin DSL и каталог `gradle/libs.versions.toml`: AGP 9.0.1,
  Kotlin/Compose compiler 2.3.20, Compose BOM 2026.03.01, Room 3.0.3,
  Navigation 3 1.0.1, KSP 2.3.12, DataStore 1.1.7.
- Wrapper Gradle 9.1.0, Java/Kotlin toolchain 17; minSdk 24, compileSdk/targetSdk 36.
  AGP использует встроенную поддержку Kotlin; отдельного Kotlin Android plugin нет.

## Структура и архитектура

Все пути Kotlin ниже относительно `app/src/main/java/com/example/couriertracker/`.
Подход близок к MVVM с репозиторием, без отдельного domain/use-case слоя,
DI-фреймворка, сетевого клиента или синхронизации.

- `MainActivity.kt`: edge-to-edge, `CourierTrackerTheme`, запуск `MainNavigation`.
- `Navigation.kt`: вручную создаёт Room и репозитории через `remember`, передаёт
  зависимости экранам; экраны создают ViewModel через `viewModel`/фабрики.
- `NavigationKeys.kt`: маршруты Main, Services, Slots, Operations, Categories,
  Settings, Exercises. `rememberNavBackStack` + `NavDisplay`; нижняя панель
  переключает пять основных экранов, drawer открывает настройки и упражнения.
  Основной переход строит стек `[Main, выбранный экран]` (для Main — `[Main]`);
  последний основной раздел восстанавливается из DataStore.
- `data/model/`: Room-сущности и модели отношений.
- `data/database/`: `AppDatabase`, четыре DAO, миграции 1→2→3.
- `data/local/Converters.kt`: LocalDateTime ↔ epoch milliseconds в системном часовом поясе.
- `data/repository/`: контракт `DataRepository`, делегирующий DAO
  `DefaultDataRepository`, отдельный `SettingsRepository`.
- `ui/main/`: `MainScreenViewModel` выдаёт Loading/Error/Success через StateFlow;
  экран считает баланс, переиспользует `OperationItem`; здесь же `formatMoney`.
- `ui/service/`, `ui/category/`, `ui/operation/`, `ui/slot/`: экраны и ViewModel,
  `list/` для списков/карточек, `sheet/` для форм добавления;
  `service/dialog/` содержит подтверждение удаления сервиса.
  Изменения данных выполняются в `viewModelScope.launch`; чтение — через Flow.
  В OperationsScreen список читается напрямую из репозитория.
- `SlotsScreenViewModel`: Loading/Error/Success, повтор загрузки и отдельный
  SlotSaveState (Idle/Saving/Success/Error); форма закрывается после успешного сохранения.
  У остальных форм обработка сохранения проще и не унифицирована.
- `ui/AppScaffold.kt`, `AppDrawer.kt`, `AppScreen.kt`: нижняя навигация, drawer,
  общая обёртка контента. `ui/components/` и `ui/common/state/`: селектор/иконка
  типа операции, предупреждение валидации и `AddItemState`.
- `theme/`: MaterialTheme, типографика, светлая/тёмная схемы, dynamic colors на Android 12+.
- `ui/settings/`: пока только версия из `AppConfig`; `ui/exercise/`: заглушка
  «Список упражнений». Composable ExercisesScreen фактически находится в
  `ExercisesViewModel.kt`; `ExercisesScreen.kt` содержит лишь package, ViewModel упражнений нет.
- `app/src/main/res/`: строки, темы, иконки и XML; manifest задаёт MainActivity.
  `app/src/test/` — локальные тесты; `app/src/androidTest/` — Compose UI-тесты.
- Корневые Gradle-файлы и `gradle/` — сборка/версии/wrapper;
  `.github/workflows/android.yml` собирает debug APK на Java 17.
  `build/`, `.gradle/` и генерируемые KSP-файлы не редактировать.

## Данные и хранение

- `Service`: id, name. `Slot`: id, serviceId, startTime/endTime (LocalDateTime).
  Внешний ключ Slot→Service с CASCADE: удаление сервиса удаляет его слоты.
- `Category`: id, name, тип INCOME/EXPENSE. `Operation`: id, nullable slotId/categoryId,
  тип, amount (`Long`, копейки), date (`Long`, **epoch day**, не milliseconds).
  Форма операции вводит целые рубли и умножает на 100.
- `SlotWithService` и `OperationWithCategory` используют Room `@Relation`;
  соответствующие DAO-запросы помечены `@Transaction`. У операции внешних ключей
  нет: не предполагать каскадное удаление операций вместе с категорией или слотом.
  Текущая форма операции slotId не заполняет.
- База `courier_tracker.db`, версия 3; миграции подключены в `MainNavigation`,
  там же при открытии включается `PRAGMA foreign_keys=ON`.
  Чтение DAO возвращает Flow, вставки/удаления — suspend.
- DataStore `settings`: последний основной экран, тип операции (по умолчанию EXPENSE),
  выбранный сервис. Удаление выбранного сервиса очищает сохранённый id.

## Правила изменений

- Сначала читать затрагиваемый код; соблюдать локальный стиль, имена и структуру.
  В проекте встречаются отступы в 2 и 4 пробела — не переформатировать соседний код.
- Переиспользовать существующие карточки, списки, формы, общие компоненты и тему;
  сохранять русский язык интерфейса и текущие соглашения о денежных суммах/датах.
- Передавать зависимости существующим способом; операции хранения оставлять
  в DAO/репозиториях, изменения — во ViewModel. Новые подписки UI делать с учётом lifecycle.
- Делать минимальные изменения по задаче; не добавлять зависимости, модули,
  архитектурные слои и масштабные рефакторинги без необходимости.
  Новые зависимости при необходимости оформлять через version catalog.
- При изменении сущностей учитывать существующие данные, версию базы и миграции;
  не заменять миграции разрушительным пересозданием базы.
- Не редактировать локальные настройки SDK, секреты, сборочные артефакты.
  Проверять поведение изменённого сценария; сообщать о непроведённых проверках.

## Сборка и проверки

Из корня, через wrapper; нужны JDK 17 и Android SDK 36, путь SDK настраивается локально.
Команды сверены с Android application plugin, модулем и debug build type;
при подготовке этого файла Gradle **не запускался**, успешность не подтверждена.

```sh
./gradlew :app:assembleDebug --stacktrace  # debug APK; эта команда есть в CI
./gradlew :app:lintDebug                  # Android Lint
./gradlew :app:testDebugUnitTest          # локальные JVM-тесты
./gradlew :app:connectedDebugAndroidTest  # UI-тесты: устройство/эмулятор и runner
```

Ограничения текущего состояния:

- В `MainScreenViewModelTest` тесты закомментированы; UI-тест — устаревшая
  заготовка с отключённым setContent и ожиданиями Hello Sample. Это не подтверждение
  работоспособности приложения. В defaultConfig не задан testInstrumentationRunner,
  хотя зависимости AndroidX runner подключены; перед запуском UI-тестов это нужно проверить.
- minSdk 24, но java.time и ряд экранов требуют API 26 через `@RequiresApi`;
  core library desugaring не настроен. Поддержка API 24–25 не подтверждена.
- AppConfig.VERSION (`0.0.2`) отличается от Gradle versionName (`1.0`).
  Не считать отображаемую версию автоматически связанной с версией APK.
- Экспорт Room-схем через schemaDirectory/KSP arguments не настроен;
  сохранённых схем и тестов миграций в исходниках нет.
