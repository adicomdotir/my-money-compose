package ir.adicom.mymoney.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object AddTransaction : Screen("add_transaction")
    data object Detail : Screen("detail/{id}") {
        fun createRoute(id: Long) = "detail/$id"
    }

    data object EditTransaction : Screen("edit_transaction/{id}") {
        fun createRoute(id: Long) = "edit_transaction/$id"
    }
}