package Validações

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class Geolocalização(context: Context){
    private var fusedLocationProviderClient: FusedLocationProviderClient

    init {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    }


    // Função para verificar se a localização está dentro da área da PUC Campinas
    private fun isInPucCampinasArea(currentLatitude: Double, currentLongitude: Double): Boolean {
        val pucCampinasLatitude = -22.8344414
        val pucCampinasLongitude = -47.0480918

        // Definir o raio como a distância máxima entre as coordenadas da PUC Campinas e qualquer ponto da PUC Campinas
        val pucCampinasRadius = 10000
        val results = FloatArray(1)
        Location.distanceBetween(
            currentLatitude,
            currentLongitude,
            pucCampinasLatitude,
            pucCampinasLongitude,
            results
        )
        val distanceInMeters = results[0]
        return distanceInMeters <= pucCampinasRadius
    }


    fun fecthLocation(context: Context, callback: (Boolean) -> Unit) {
        // Verificar permissões de localização
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Se as permissões não estiverem concedidas, solicitar permissões de localização
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                100
            )
            return
        }

        // Criar uma requisição de localização
        val locationRequest = LocationRequest.create().apply {
            interval = 10000 // Intervalo de atualização em milissegundos (10 segundos)
            fastestInterval = 5000 // Intervalo mais rápido em milissegundos (5 segundos)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY // Alta precisão
        }

        // Iniciar as atualizações de localização
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    // Lidar com as atualizações de localização recebidas
                    val currentLocation = locationResult.lastLocation
                    if (currentLocation != null) {
                        // Verificar se a localização está dentro da área da PUC Campinas
                        val isInPucCampinas = isInPucCampinasArea(currentLocation.latitude, currentLocation.longitude)
                        callback(isInPucCampinas)// Chama o callback passando o resultado da verificação
                        if (isInPucCampinas) {
                            Log.i("Latitude", "Esta Na Puc. fecthLocation: ${currentLocation.latitude}")
                            Log.i("Longitude", "Esta Na Puc. fecthLocation: ${currentLocation.longitude}")
                        } else {
                            Log.i("Latitude", "Não esta na Puc. fecthLocation: ${currentLocation.latitude}")
                            Log.i("Longitude", "Não esta na Puc. fecthLocation: ${currentLocation.longitude}")
                        }
                    }
                }
            },
            null
        )
    }
}