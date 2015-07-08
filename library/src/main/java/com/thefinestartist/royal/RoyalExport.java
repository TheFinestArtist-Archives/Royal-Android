package com.thefinestartist.royal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by TheFinestArtist on 7/8/15.
 */
public class RoyalExport {

    // http://stackoverflow.com/a/29849902/1797648
    // http://stackoverflow.com/users/2945594/bokebe
    public static void sendEmail(@NonNull Context context) {
//        sendEmail(context, null, realms);
    }

    public static void sendEmail(@NonNull Context context, String email) {
//        sendEmail(context, email, null, realms);
    }

    public static void sendEmail(@NonNull Context context, String email, Intent intent) {
//        sendEmail(context, email, intent, realms);
    }

    public static void sendEmail(@NonNull Context context, @NonNull Realm... realms) {
        sendEmail(context, null, realms);
    }

    public static void sendEmail(@NonNull Context context, String email, @NonNull Realm... realms) {
        sendEmail(context, email, null, realms);
    }


    // TODO: Add device information
    public static void sendEmail(@NonNull Context context, String email, Intent intent, @NonNull Realm... realms) {
        try {
            // Exporting .realm files into cache directory
            ArrayList<Uri> uris = new ArrayList<>();
            for (Realm realm : realms) {
                String fileName = realm.getConfiguration().getRealmFileName();
                File realmFile = new File(context.getExternalCacheDir(), fileName);
                realmFile.delete();
                realm.writeCopyTo(realmFile);
                uris.add(Uri.fromFile(realmFile));
            }

            // Intent Initialize
            if (intent == null)
                intent = new Intent();

            // Setting Intent Action and Type
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);

            // Attaching files
            ArrayList<Uri> attached = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
            if (attached != null)
                uris.addAll(attached);
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

            if (intent.getType() == null)
                intent.setType("message/rfc822");

            // Set email
            if (intent.getStringArrayExtra(Intent.EXTRA_EMAIL) == null)
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});

            // Subject build
            if (intent.getStringExtra(Intent.EXTRA_SUBJECT) == null) {
                StringBuilder subject = new StringBuilder();
                subject.append("[Royal] Exported ");
                subject.append(uris.size());
                subject.append(" realm file");
                if (uris.size() > 1)
                    subject.append("s");
                intent.putExtra(Intent.EXTRA_SUBJECT, subject.toString());
            }

            // Message build
            if (intent.getStringExtra(Intent.EXTRA_TEXT) == null) {
                StringBuilder message = new StringBuilder();
                for (Realm realm : realms) {
                    RealmConfiguration configuration = realm.getConfiguration();

                    message.append("giName: ");
                    message.append(configuration.getRealmFileName());
                    message.append("\n");

                    message.append("Version: ");
                    message.append(configuration.getSchemaVersion());
                    message.append("\n");

                    message.append("Path: ");
                    message.append(configuration.getPath());
                    message.append("\n");

                    message.append("Encryption Key: ");
                    message.append(Arrays.toString(configuration.getEncryptionKey()));
                    message.append("\n");

                    message.append("\n");
                }
                intent.putExtra(Intent.EXTRA_TEXT, message.toString());
            }

            context.startActivity(Intent.createChooser(intent, "Choose Email Application"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    TODO: HTML format
//    final Intent shareIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
//    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "The Subject");
//    shareIntent.putExtra(
//    Intent.EXTRA_TEXT,
//            Html.fromHtml(new StringBuilder()
//    .append("<p><b>Some Content</b></p>")
//    .append("<small><p>More content</p></small>")
//    .toString())
//            );
//
//
//    message.append("<table border=\"1\" style=\"background-color:#E4E5E6;border-collapse:collapse;border:1px solid #000000;color:#000000;width:100%\" cellpadding=\"8\" cellspacing=\"0\">");
//
//    message.append("<tr><td>Name</td><td>");
//    message.append(configuration.getRealmFileName());
//    message.append("</td></tr>");
//
//    message.append("<tr><td>Version</td><td>");
//    message.append(configuration.getSchemaVersion());
//    message.append("</td></tr>");
//
//    message.append("<tr><td>Path</td><td>");
//    message.append(configuration.getPath());
//    message.append("</td></tr>");
//
//    message.append("<tr><td>Encryption Key</td><td>");
//    message.append(Arrays.toString(configuration.getEncryptionKey()));
//    message.append("</td></tr>");
//
//    message.append("</table>");

    // http://stackoverflow.com/a/30364742/1797648
    // http://stackoverflow.com/users/1574527/rooney
    // http://stackoverflow.com/users/1545363/nayoso
    public static void moveTo(@NonNull Context context, @NonNull Realm realm, @NonNull String path) {
        File f = new File(realm.getPath());
        if (f.exists())
            copy(f, new File(Environment.getExternalStorageDirectory() + "/default.realm"));
    }

    public static void copy(File src, File dst) {
        try {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dst);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
