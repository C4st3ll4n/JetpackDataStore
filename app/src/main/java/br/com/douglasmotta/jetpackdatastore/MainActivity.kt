package br.com.douglasmotta.jetpackdatastore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.createDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import br.com.douglasmotta.jetpackdatastore.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val dataStore: DataStore<Preferences> = createDataStore("settings")

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        binding.run {
            buttonSave.setOnClickListener{
                lifecycleScope.launch{
                    val key =  inputKey.text?.toString()?: ""
                    val value = inputValue.text?.toString()?:""
                    save(key,value)
                }
            }

            buttonRead.setOnClickListener{
                lifecycleScope.launch{
                    val key = inputReadKey.text?.toString()?:""
                    textDataStoredValue.text = read(key) ?: "Nenhum valor encontrado"
                }
            }
        }
    }

    private suspend fun save(key:String, value:String){
        val prefsKey = preferencesKey<String>(key)
        dataStore.edit {
            settings -> settings[prefsKey] = value
        }
    }

    private suspend fun read(key:String): String?{
        val prefsKey = preferencesKey<String>(key)
        val prefs = dataStore.data.first()
        return prefs[prefsKey]
    }
}