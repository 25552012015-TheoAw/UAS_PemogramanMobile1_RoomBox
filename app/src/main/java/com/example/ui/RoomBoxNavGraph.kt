package com.example.ui

/**
 * Project: RoomBox (Manajemen Paket Apartemen)
 * Author : TheoAleksanderWilliam
 * NIM    : 25552012015
 */

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

object RoomBoxRoutes {
    const val SPLASH = "splash"
    const val DASHBOARD = "dashboard"
    const val MANAGE_UNIT = "manage_unit"
    const val ADD_PACKET = "add_packet"
    const val PACKET_LIST = "packet_list"
    const val DETAIL_PACKET = "detail_packet/{packetId}"

    fun detailPacket(packetId: Int) = "detail_packet/$packetId"
}

@Composable
fun RoomBoxNavGraph(
    viewModel: RoomBoxViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = RoomBoxRoutes.SPLASH
    ) {
        composable(RoomBoxRoutes.SPLASH) {
            SplashScreen(
                onTimeout = {
                    navController.navigate(RoomBoxRoutes.DASHBOARD) {
                        popUpTo(RoomBoxRoutes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(RoomBoxRoutes.DASHBOARD) {
            DashboardScreen(
                viewModel = viewModel,
                onNavigateManageUnit = { navController.navigate(RoomBoxRoutes.MANAGE_UNIT) },
                onNavigateAddPacket = { navController.navigate(RoomBoxRoutes.ADD_PACKET) },
                onNavigateAllPackets = {
                    viewModel.setStatusFilter("SEMUA")
                    viewModel.setSearchQuery("")
                    navController.navigate(RoomBoxRoutes.PACKET_LIST)
                },
                onNavigateSearchPacket = {
                    viewModel.setStatusFilter("SEMUA")
                    viewModel.setSearchQuery("")
                    navController.navigate(RoomBoxRoutes.PACKET_LIST)
                }
            )
        }

        composable(RoomBoxRoutes.MANAGE_UNIT) {
            ManageUnitScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(RoomBoxRoutes.ADD_PACKET) {
            AddPacketScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSuccessSaved = { navController.popBackStack() }
            )
        }

        composable(RoomBoxRoutes.PACKET_LIST) {
            PacketListScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSelectPacket = { packetId ->
                    navController.navigate(RoomBoxRoutes.detailPacket(packetId))
                }
            )
        }

        composable(
            route = RoomBoxRoutes.DETAIL_PACKET,
            arguments = listOf(navArgument("packetId") { type = NavType.IntType })
        ) { backStackEntry ->
            val packetId = backStackEntry.arguments?.getInt("packetId") ?: 0
            DetailPacketScreen(
                viewModel = viewModel,
                packetId = packetId,
                onBack = { navController.popBackStack() },
                onSuccessDeletedOrUpdated = { navController.popBackStack() }
            )
        }
    }
}
