package com.example.couriertracker.ui.service.sheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.couriertracker.data.exception.ServiceAlreadyExistsException
import com.example.couriertracker.data.model.Service
import com.example.couriertracker.data.repository.DataRepository
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddServiceSheetRoute(
    repository: DataRepository,
    onDismiss: () -> Unit
) {
    val viewModel: AddServiceViewModel = viewModel { AddServiceViewModel(repository) }
    val scope = rememberCoroutineScope()
    var isSaving by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val onSave: (Service) -> Unit = { service ->
        if (!isSaving) {
            isSaving = true
            scope.launch {
                try {
                    viewModel.saveService(service)
                    onDismiss()
                } catch (e: CancellationException) {
                    throw e
                } catch (e: ServiceAlreadyExistsException) {
                    isSaving = false
                    snackbarHostState.showSnackbar(e.message.orEmpty())
                } catch (e: Exception) {
                    isSaving = false
                    snackbarHostState.showSnackbar("Не удалось сохранить счет")
                } finally {
                    isSaving = false
                }
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = {
            if (!isSaving) {
                onDismiss()
            }
        },
        sheetGesturesEnabled = !isSaving,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = !isSaving,
            shouldDismissOnClickOutside = !isSaving
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            AddServiceSheet(onSave = onSave, isSaving = isSaving)

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}