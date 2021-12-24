package com.example.depthmapping;

        import android.annotation.SuppressLint;
        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Matrix;
        import android.util.Base64;
        import android.util.Log;

        import com.example.depthmapping.ui.home.HomeFragment;

        import org.apache.sshd.client.SshClient;
        import org.apache.sshd.client.channel.ClientChannel;
        import org.apache.sshd.client.channel.ClientChannelEvent;
        import org.apache.sshd.client.session.ClientSession;
        import org.apache.sshd.common.channel.Channel;
        import org.apache.sshd.server.forward.AcceptAllForwardingFilter;

        import java.io.ByteArrayOutputStream;
        import java.io.IOException;
        import java.io.OutputStream;
        import java.io.OutputStreamWriter;
        import java.util.EnumSet;
        import java.util.concurrent.TimeUnit;

public class SshConection{

    String host;
    int port;
    String username;
    String password;
    Context context;
    Bitmap bitmap;
    Bitmap bitmap_out;



    String string_out;

    public SshConection(String host, int port, String username, String password, Context context, Bitmap bitmap) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.context = context;
        this.bitmap = bitmap;
    }

    public Bitmap start() {


        String base64String = getBase64String(bitmap);


        String command = "python /content/BoostingMonocularDepth/server.py --input_picture " + base64String + " \n";

        String key = "user.home";
        Context Syscontext;
        Syscontext = context;
        String val = Syscontext.getApplicationInfo().dataDir;
        System.setProperty(key, val);


        SshClient client = SshClient.setUpDefaultClient();
        client.setForwardingFilter(AcceptAllForwardingFilter.INSTANCE);
        client.start();

        Thread thread = new Thread(new Runnable() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void run() {
                try {


                    try (ClientSession session = client.connect(username, host, port).verify(10000).getSession()) {
                        session.addPasswordIdentity(password);
                        session.auth().verify(150000);
                        System.out.println("Connection establihed");


                        ClientChannel channel = session.createChannel(Channel.CHANNEL_SHELL);
                        System.out.println("Starting shell");

                        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
                        channel.setOut(responseStream);

                        channel.open().verify(180, TimeUnit.SECONDS);
                        try (OutputStream pipedIn = channel.getInvertedIn()) {
                            pipedIn.write(command.getBytes());
                            pipedIn.flush();
                        }


                        channel.waitFor(EnumSet.of(ClientChannelEvent.CLOSED),
                                TimeUnit.SECONDS.toMillis(180));


                        String responseString = new String(responseStream.toByteArray());
                        System.out.println("__________________________________________");
                        System.out.println(getNormResp(responseString));
                        System.out.println("__________________________________________");
                        writeToFile(getNormResp(responseString), context);
                        bitmap_out = convert(getNormResp(responseString));
                        HomeFragment.bitmap = bitmap_out;
//                        HomeFragment.binding.imageView.setImageResource(R.drawable.header);




                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        client.stop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        return bitmap_out;
    }

     static String getBase64String(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String base64String = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        return base64String;
    }

    public static Bitmap convert(String base64Str) throws IllegalArgumentException
    {
        byte[] decodedBytes = Base64.decode( base64Str.substring(base64Str.indexOf(",") + 1), Base64.DEFAULT );
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String getNormResp(String s){
        int indexStart = s.indexOf("StartTextPoint");
        int indexEnd = s.indexOf("EndTextPoint");
        s = s.substring(indexStart + 14, indexEnd);
        return s;
    }

    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

}

