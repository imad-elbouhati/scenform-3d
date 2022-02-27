package com.imadev.helloscenform

import android.app.ActivityManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.ar.core.Anchor
import com.google.ar.core.HitResult
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.coroutines.*
import java.util.concurrent.CompletableFuture


class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.simpleName
        private val MIN_OPENGL_VERSION = 3.0
    }


    private var buttonRenderable: ViewRenderable? = null

    private lateinit var arFragment: ArFragment
    private lateinit var andyRenderable: ModelRenderable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!checkIsSupportedDeviceOrFinish(this)) return

        arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment


        // Build a renderable from a 2D View.

        setUpRenderable()

        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->

            addNodeToScene(hitResult, andyRenderable)
        }


    }

    private fun setUpRenderable() {
        ModelRenderable.builder()
            .setSource(this, R.raw.andy)
            .build()
            .thenAccept { renderable ->
                andyRenderable = renderable
            }.exceptionally { throwble ->
                val toast =
                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                null
            }

    }


    private fun addNodeToScene(hitResult: HitResult, modelRenderable: ModelRenderable) {
        val anchor = hitResult.createAnchor()
        val anchorNode = AnchorNode(anchor)
        anchorNode.setParent(arFragment.arSceneView.scene)

        val node = TransformableNode(arFragment.transformationSystem)


        val buttonNode = Node()
        buttonNode.setParent(node)

        val buttonRenderable = createDeleteButtonRenderable()
        if(buttonRenderable == null) {
            Toast.makeText(this, "Tap again", Toast.LENGTH_SHORT).show()
            return
        }

        node.setParent(anchorNode)
        node.renderable = modelRenderable
        node.select()

        buttonNode.renderable = buttonRenderable
        buttonNode.localPosition = Vector3(0.0f, 0.25f, 0.0f)


        buttonRenderable?.view?.setOnClickListener {
            Toast.makeText(this, "To delete", Toast.LENGTH_SHORT).show()
            node.setParent(null)
        }

        node.setOnTapListener { p0, p1 ->
            Log.d(TAG, "onTap: ${p0?.node?.name}")

            buttonNode.isEnabled = !buttonNode.isEnabled
        }

    }


    private fun createDeleteButtonRenderable(): ViewRenderable? {

        val button = Button(this).also {
            it.text = "Delete"
            it.setBackgroundColor(Color.RED)
            it.setTextColor(Color.WHITE)
        }

        val buttonStage = ViewRenderable.builder().setView(this, button).build()

        CompletableFuture.allOf(
            buttonStage
        ).handle { void, throwable ->
            if (throwable != null) {
                Toast.makeText(this, "Unable to load delete button", Toast.LENGTH_SHORT).show()
                return@handle
            }

            buttonRenderable = buttonStage.get()


        }.exceptionally {
            if (it != null) {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                it.printStackTrace()
                return@exceptionally
            }
        }


        return buttonRenderable
    }


    private fun checkIsSupportedDeviceOrFinish(activity: MainActivity): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later")
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG)
                .show()
            activity.finish()
            return false
        }

        val openGlVersionString =
            (activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
                .deviceConfigurationInfo
                .glEsVersion

        if (openGlVersionString.toDouble() < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later")
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                .show()
            activity.finish()
            return false
        }

        return true
    }


}


