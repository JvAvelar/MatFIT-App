package engsoft.matfit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = Screen.Login) {
//        composable(Screen.Home.route) {
//            // ARQUIVO da TELA
//
//        }
    }

}

sealed class Screen(val route: String) {
    object Home: Screen(Routes.HOME)
    object Register: Screen(Routes.REGISTER)
    object Login: Screen(Routes.LOGIN)
    object Add_Student: Screen(Routes.ADD_STUDENT)
    object Add_Equipment: Screen(Routes.ADD_EQUIPMENT)
    object Add_Employee: Screen(Routes.ADD_EMPLOYEE)
}

private object Routes {
    const val HOME = "home"
    const val REGISTER = "register"
    const val LOGIN = "login"
    const val ADD_STUDENT = "addStudent"
    const val ADD_EQUIPMENT = "addEquipment"
    const val ADD_EMPLOYEE = "addEmployee"

}