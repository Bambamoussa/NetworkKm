package fr.istic.mob.networkKM

import android.app.AlertDialog
import android.app.Dialog
import android.content.ClipData
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.view.menu.MenuView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.res.ResourcesCompat
import fr.istic.mob.networkKM.modele.Connexion
import fr.istic.mob.networkKM.modele.Graph
import fr.istic.mob.networkKM.modele.Node
import fr.istic.mob.networkKM.modele.Position
import androidx.appcompat.view.menu.MenuView.ItemView as MenuViewItemView

private const val TAG = "DrawableGraph1"
class DrawableGraph1 : View {

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

    }
    lateinit var graphe2:Graph
    private var path = Path()
    private var currentX = 0f
    private var currentY = 0f
    private var objet1 :Node?=null
    private var objet2:Node?=null
    private var listConnection =mutableListOf<Connexion>()


    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f

    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap
    private lateinit var frame: RectF
    private var listRectF =mutableListOf<Node>()

    // Set up the paint with which to draw.
    private val paint = Paint().apply {
        color = ResourcesCompat.getColor(resources,
            R.color.colorPaint, null)
        // Smooths out edges of what is drawn without affecting shape.
        isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        isDither = true
        style = Paint.Style.FILL // default: FILL
        strokeJoin = Paint.Join.ROUND // default: MITER
        strokeCap = Paint.Cap.ROUND // default: BUTT



    }
    private val textPaint1 = Paint().apply {
        color = ResourcesCompat.getColor(resources,
            R.color.colorPaint, null)
            textSize=70f

    }

    private val paint1 = Paint().apply {
        color = ResourcesCompat.getColor(resources,
            R.color.colorPaint, null)

           style = Paint.Style.STROKE // default: FILL



    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        listRectF.forEach {
            canvas.drawRect(it.form,paint)
            //pour l'affichage du text
            //canvas.drawText()
            canvas.drawText(it.label,it.form.top  + 25,it.form.centerY(),textPaint1)

        }
        listConnection.forEach {
            canvas.drawLine(it.objet1.position.x,it.objet1.position.y,it.objet2.position.x,it.objet2.position.y,paint)

        }
         canvas.drawPath(path,paint1)


        invalidate()
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)

        if (::extraBitmap.isInitialized) extraBitmap.recycle()
        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        // extraCanvas.drawColor(backgroundColor)


    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        currentX=event.x
        currentY=event.y

        invalidate()
        return super.onTouchEvent(event)
    }


    private fun touchStart() {
        Log.d(TAG, "touchStart: ")
        path.reset()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        objet1 = getObjet(motionTouchEventX, motionTouchEventY)
    }
/*
    private fun getObjet(x:Float,y:Float){
        listConnection.forEach { obj->

         if(obj.thing.contains(x,y))
             return obj
        }
   return null

    }*/

    private fun touchMove() {
        Log.d(TAG, "touchMove: ")
        val dx = Math.abs(motionTouchEventX - currentX)
        val dy = Math.abs(motionTouchEventY - currentY)

        // QuadTo() adds a quadratic bezier from the last point,
        // approaching control point (x1,y1), and ending at (x2,y2).
        path.lineTo(motionTouchEventX,motionTouchEventY)
        invalidate()
    }

    private fun touchUp() {

         objet2 = getObjet(motionTouchEventX, motionTouchEventY)
        if (objet1!= null && objet2  != null) {
            // creer une connexion et l'ajouter a la liste de connexion
            listConnection.add(Connexion(objet1!!,objet2!!))

        }
        path.reset()
    }

    private  fun getObjet(motionTouchEventX:Float,motionTouchEventY:Float): Node? {
        listRectF.forEach {
                 node: Node ->
            if (node.form.contains(motionTouchEventX,motionTouchEventY) ){

                return node
            }


        }
        return null
    }

    fun createObjet(label:String) {
        Log.d("TAG", "createObjet: ")
        // Calculate a rectangular frame around the picture.

        val inset = 100f

        val frame = RectF(currentX, currentY, currentX+ inset, currentY +inset)

       listRectF.add(Node(frame,0, Position(currentX,currentY),label))
        //graphe2.nodes.add(Node(frame,0, Position(currentX,currentY),label))

        invalidate()

    }




    fun createConnexion(event: MotionEvent) {

        motionTouchEventX = event.x
        motionTouchEventY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }
    }

    fun reinitialisation() {
             listRectF.clear()
             listConnection.clear()

    }


}