package ir.adicom.mymoney.ui

sealed interface OperationState {
    data object Idle : OperationState
    data object Adding : OperationState
    data object Deleting : OperationState
    data object Updating : OperationState
    data class Error(val message: String) : OperationState
}