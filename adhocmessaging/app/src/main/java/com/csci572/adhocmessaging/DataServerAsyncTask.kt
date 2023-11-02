import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.widget.TextView
import java.io.File
import java.io.FileOutputStream
import java.net.ServerSocket

class FileServerAsyncTask(
    private val context: Context,
    private var statusText: TextView
) : AsyncTask<Void, Void, String?>() {

    override fun doInBackground(vararg params: Void): String? {
        /**
         * Create a server socket.
         */
        val serverSocket = ServerSocket(8888)
        return serverSocket.use {
            /**
             * Wait for client connections. This call blocks until a
             * connection is accepted from a client.
             */
            val client = serverSocket.accept()
            /**
             * If this code is reached, a client has connected and transferred data
             * Save the input stream from the client as a JPEG file
             */
            val f = File(
                Environment.getExternalStorageDirectory().absolutePath +
                    "/${context.packageName}/wifip2pshared-${System.currentTimeMillis()}.jpg")
            val dirs = File(f.parent)

            dirs.takeIf { it.doesNotExist() }?.apply {
                mkdirs()
            }
            f.createNewFile()
            val inputStream = client.getInputStream()
            //copyFile(inputStream, FileOutputStream(f))
            val outputStream = FileOutputStream(f)
            var buf = ByteArray(1024)
            var len: Int
            while (inputStream.read(buf).also { len = it } != -1) {
                outputStream.write(buf, 0, len)
            }
            outputStream.close()
            serverSocket.close()
            f.absolutePath
        }
    }

    private fun File.doesNotExist(): Boolean = !exists()

    /**
     * Start activity that can handle the JPEG image
     */
    override fun onPostExecute(result: String?) {
        result?.run {
            statusText.text = "File copied - $result"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(Uri.parse("file://$result"), "image/*")
            }
            context.startActivity(intent)
        }
    }
}