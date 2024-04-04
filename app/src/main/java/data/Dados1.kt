package data

import android.os.Parcelable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.parcelize.Parcelize

@Parcelize
data class Dados1(
    var dia: String? = null,
    var hre: Int = 0,
    var hrs: Int = 0
) : Parcelable, ValueEventListener {
    override fun onDataChange(snapshot: DataSnapshot) {
        TODO("Not yet implemented")
    }

    override fun onCancelled(error: DatabaseError) {
        TODO("Not yet implemented")
    }
}
