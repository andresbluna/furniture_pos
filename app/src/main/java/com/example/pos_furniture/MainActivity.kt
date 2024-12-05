package com.example.pos_furniture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pos_furniture.navigation.NavRoutes
import com.example.pos_furniture.ui.theme.POS_furnitureTheme
import java.io.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            POS_furnitureTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") { LoginScreen(navController) }
                    composable("furnitureList") { FurnitureListScreen(navController) }
                    composable("invoice") { InvoiceScreen(navController) }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.furniture_001),
            contentDescription = "Furniture Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        BasicTextField(
            value = username,
            onValueChange = { username = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .background(Color(0xFFD7BFAE))
                        .padding(8.dp)
                ) {
                    if (username.isEmpty()) {
                        Text("Usuario", color = Color.Gray)
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            visualTransformation = PasswordVisualTransformation(),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .background(Color.LightGray)
                        .padding(8.dp)
                ) {
                    if (password.isEmpty()) {
                        Text("Password")
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(NavRoutes.FurnitureList.route) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF8B5E3C),
                contentColor = Color.White
            )
        ) {
            Text("Ingresar")
        }
    }
}

@Composable
fun FurnitureItem(
    furniture: Furniture,
    onSelect: (Furniture) -> Unit
) {
    var quantity by remember { mutableStateOf(1) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onSelect(furniture) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = furniture.imageResId),
                contentDescription = furniture.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = furniture.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$ ${String.format("%.2f", furniture.price)}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = furniture.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { if (quantity > 1) quantity-- },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("-")
                }
                Text(
                    text = quantity.toString(),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Button(
                    onClick = { quantity++ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+")
                }
            }
        }
    }
}

@Composable
fun FurnitureListScreen(navController: NavHostController) {
    var selectedFurniture by remember { mutableStateOf<Furniture?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            items(items = FurnitureList.furnitureList) { furniture ->
                FurnitureItem(
                    furniture = furniture,
                    onSelect = { selectedFurniture = it }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Button(
            onClick = {
                if (selectedFurniture != null) {
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "selectedFurniture",
                        selectedFurniture
                    )
                    navController.navigate("invoice")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            enabled = selectedFurniture != null
        ) {
            Text("Comprar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    POS_furnitureTheme {
        val navController = rememberNavController()
        LoginScreen(navController)
    }
}

data class Furniture(
    val id: Int,
    val name: String,
    val price: Double,
    val description: String,
    val imageResId: Int
) : Serializable

object FurnitureList {
    val furnitureList = listOf(
        Furniture(1, "Mesa Calida", 150.3000, "Sofá de 3 plazas en tela gris", R.drawable.furniture_007),
        Furniture(2, "Repisa dodge", 399.9990, "Mesa extensible de madera maciza", R.drawable.furniture_002),
        Furniture(3, "Sillon de madera", 89.9900, "Silla ergonómica tapizada", R.drawable.furniture_003),
        Furniture(4, "Decorador de sala", 899.9930, "Cama con cabecera acolchada", R.drawable.furniture_004),
        Furniture(5, "Mesa redonda", 499.99340, "Armario de 3 puertas con espejo", R.drawable.furniture_005),
        Furniture(6, "Silla Bauhg", 79.9940, "Mesa auxiliar con 2 cajones", R.drawable.furniture_006),
        Furniture(7, "Mesa longe", 299.9490, "Escritorio con cajones y estante", R.drawable.furniture_007),
        Furniture(8, "Sala decorador", 199.9490, "Estantería de 5 niveles", R.drawable.furniture_004),
        Furniture(9, "Sillón Reclinable", 4494.990, "Sillón con sistema reclinable", R.drawable.furniture_009),
        Furniture(10, "Silla bisoñé", 1494.990, "Mesa de centro con vidrio templado", R.drawable.furniture_010)
    )
}

@Composable
fun InvoiceScreen(navController: NavHostController) {
    val selectedFurniture = navController.previousBackStackEntry?.savedStateHandle?.get<Furniture>("selectedFurniture")
    val quantity = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("quantity") ?: 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Detalle de Compra",
            style = MaterialTheme.typography.headlineMedium
        )

        if (selectedFurniture != null) {
            Text(
                text = """
                    Descripción: ${selectedFurniture.name}
                    Cantidad x Precio Subtotal
                    ------------------------------------------
                    ${quantity} x ${String.format("%.2f", selectedFurniture.price)} ${String.format("%.2f", quantity * selectedFurniture.price)}
                    Valor Neto ${String.format("%.2f", quantity * selectedFurniture.price)}
                    Iva ${String.format("%.2f", quantity * selectedFurniture.price * 0.12)}
                    Total $ ${String.format("%.2f", quantity * selectedFurniture.price + quantity * selectedFurniture.price * 0.12)}
                """.trimIndent(),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { navController.navigateUp() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}
