import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.widget.TextView
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.InetSocketAddress
import java.net.Socket

class TransferData(
    private val context: Context,
    private var statusText: TextView,
    private val host: String,
    private val port: Int
) : AsyncTask<Void, Void, String?>() {

    override fun doInBackground(vararg params: Void): String? {
        var len: Int
        var buf = ByteArray(1024)
        val socket = Socket()

        try {
            /**
             * Create a client socket with the host,
             * port, and timeout information.
             */
            socket.bind(null)
            socket.connect((InetSocketAddress(host, port)), 500)

            /**
             * Create a byte stream from a JPEG file and pipe it to the output stream
             * of the socket. This data is retrieved by the server device.
             */
            val outputStream = socket.getOutputStream()
            val cr = context.contentResolver
            val inputStream: InputStream? = cr.openInputStream(Uri.parse("path/to/picture.jpg"))
            while (inputStream!!.read(buf).also { len = it } != -1) {
                outputStream.write(buf, 0, len)
            }
            outputStream.close()
            inputStream.close()
        } catch (e: FileNotFoundException) {
            //catch logic
        } catch (e: IOException) {
            //catch logic
        } finally {
            /**
             * Clean up any open sockets when done
             * transferring or if an exception occurred.
             */
            socket.takeIf { it.isConnected }?.apply {
                close()
            }
        }

        return null;
    }
}