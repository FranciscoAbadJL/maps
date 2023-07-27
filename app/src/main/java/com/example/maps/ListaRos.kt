package com.example.maps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maps.databinding.ActivityListaRosBinding

class ListaRos : AppCompatActivity() {

    private lateinit var binding: ActivityListaRosBinding

    private lateinit var adapter: RecyclerAdapter
    private val rosticeriasmtz = mutableListOf<Rosticerias>()


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState)
        binding = ActivityListaRosBinding.inflate(layoutInflater)
        setContentView(binding.root)


        rosticeriasmtz.add(Rosticerias("ROSTICERIA DE POLLOS LUPITA", 20.06394745, -97.05683784))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA DE POLLOS ROSTICERIA DEIVY", 20.06272211, -97.05701326))
        rosticeriasmtz.add(Rosticerias("ROSTICERÍA EL ASADOR", 20.12947963, -96.99445532))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA EL FOGON", 20.06119051, -97.05798803))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA EL FOGON", 20.07046022, -97.06280869))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA EL PESCADOR", 20.05843286, -97.05291111))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA EL POLLO BRONCO", 20.06280747, -97.05328847))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA EL POLLON", 20.06772488, -97.04767304))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA EL PUENTE", 20.05877158, -97.05343994))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA EL PUENTE", 20.06559456, -97.04984646))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA LA LLAMARADA", 20.07420713, -97.06300038))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA LA LLAMARADA", 20.065629, -97.0494147))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA LA PALAPA NORTEÑA", 20.06274651, -97.05572105))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA LA PALAPA NORTEÑA", 20.07395274, -97.06263125))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA LUPITA", 20.07299227, -97.0645257))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA MR POLLO", 20.06130814, -97.05714042))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA PIPIOLOS", 20.06400393, -97.05648428))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA POLLOS A LA LEÑA", 20.06433767, -97.05681565))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA POLLOS PIPIOLO", 20.1292851, -96.99620022))
        rosticeriasmtz.add(Rosticerias("ROSTICERIA RAMOS", 20.06337913, -97.0571883))

        adapter = RecyclerAdapter(rosticeriasmtz)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

    }
}