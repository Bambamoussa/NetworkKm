package fr.istic.mob.networkKM

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import java.util.*


class MainActivity : AppCompatActivity(), View.OnLongClickListener, View.OnTouchListener{

    lateinit var graph:DrawableGraph1
    var ajouter_connection:Boolean=false
    var ajouter_objet:Boolean=false
    var renitialisation:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadLocale()
        graph=findViewById(R.id.graph)
        graph.setOnLongClickListener(this)
        graph.setOnTouchListener(this)



    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         menuInflater.inflate(R.menu.main_menu,menu)
         return true
    }

    override fun onOptionsItemSelected(item: MenuItem):Boolean {

          when  (item.itemId)  {

            R.id.action_ajouter_connexion -> {
              ajouter_objet=false
              renitialisation=false
              ajouter_connection=  true

            }
            R.id.action_ajouter_objet -> {
                ajouter_connection=false
                renitialisation=false
                 ajouter_objet=true

            }
            R.id.action_reinitialisation -> {
                ajouter_connection=false
                ajouter_objet=false
                renitialisation=true
                    if (renitialisation) {
                        graph.reinitialisation()

                               }

                       }
              R.id.langue -> {
                  loadLocale()
                  ShowLangue()
              }

            }



        return super.onOptionsItemSelected(item)
    }

    override fun onLongClick(p0: View?): Boolean {
        Log.d("TAG", "onLongClick: ")
        if(ajouter_objet){
            Log.d("TAG", "onLongClick: ")
            ShowObjet()

        }

        return true
    }

    override fun onTouch(p0: View?, p1: MotionEvent): Boolean {
        Log.d("TAG", "onTouch: ")
         if(ajouter_connection==true){
            graph.createConnexion(p1)
        }
         return super.onTouchEvent(p1)
    }

    fun ShowObjet(){

        val builder= AlertDialog.Builder(this)
        val inflater=layoutInflater
        builder.setTitle("create Objet")
        val dialogLayout=inflater.inflate(R.layout.dialog_signin, null)
        val ediText=dialogLayout.findViewById<EditText>(R.id.objet)
        
        builder.setView(dialogLayout)
        builder.setPositiveButton("OUI"){ dialogInterface: DialogInterface, i: Int ->  graph.createObjet(ediText.text.toString())  }
        builder.setNegativeButton("NON",null)

        builder.show()

    }

    private fun ShowLangue(){
        val listItem= arrayOf("Français","English")
        val builderLangue=AlertDialog.Builder(this)
        builderLangue.setTitle("choose Language")
        builderLangue.setSingleChoiceItems(listItem,-1){

            dialog,which ->
            if(which==0){
                setLocate("Fr")
                recreate()
            }
            else if(which==1){
                setLocate("en")
                recreate()
            }
            dialog.dismiss()
        }
        val ndialog=builderLangue.create()
        ndialog.show()



    }

    private  fun setLocate(Lang:String){

        val locale =Locale(Lang)
        Locale.setDefault(locale)
        val config=Configuration()
        config.locale=locale
        baseContext.resources.updateConfiguration(config,baseContext.resources.displayMetrics)
        val editor=getSharedPreferences("Settings",Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang",Lang)
        editor.apply()
    }

    private  fun loadLocale(){
        val SharedPreferences=getSharedPreferences("Settings",Activity.MODE_PRIVATE)
        val langage=SharedPreferences.getString("My_Lang"," ")

       setLocate(langage.toString())

    }
}