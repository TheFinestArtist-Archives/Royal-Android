package com.thefinestartist.royal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.thefinestartist.royal.util.ByteUtil;
import com.thefinestartist.royal.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by TheFinestArtist on 7/8/15.
 */
public class RoyalExport {

    // http://stackoverflow.com/a/29849902/1797648
    // http://stackoverflow.com/users/2945594/bokebe
    public static void toEmail(RealmConfiguration... configurations) {
        toEmail(null, configurations);
    }

    public static void toEmail(String email, RealmConfiguration... configurations) {
        toEmail(email, null, configurations);
    }

    // Decrypt Automatically
    public static void toEmail(String email, Intent intent, RealmConfiguration... configurations) {
        if (configurations == null || configurations.length == 0)
            return;

        List<Realm> realms = new ArrayList<>();
        for (RealmConfiguration configuration : configurations)
            realms.add(Realm.getInstance(configuration));

        toEmail(email, intent, realms.toArray(new Realm[realms.size()]));

        for (Realm realm : realms)
            realm.close();
    }

    public static void toEmail(Realm... realms) {
        toEmail(null, realms);
    }

    public static void toEmail(String email, Realm... realms) {
        toEmail(email, null, realms);
    }

    // Decrypt Automatically
    // TODO: Add device information
    // TODO: HTML format
    public static void toEmail(String email, Intent intent, Realm... realms) {
        if (realms == null || realms.length == 0)
            return;

        Context context = Royal.getApplicationContext();

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

                    message.append("Name: ");
                    message.append(configuration.getRealmFileName());
                    message.append("\n");

                    message.append("Version: ");
                    message.append(configuration.getSchemaVersion());
                    message.append("\n");

                    message.append("Path: ");
                    message.append(configuration.getPath());
                    message.append("\n");

                    message.append("Encryption Key: ");
                    byte[] key = configuration.getEncryptionKey();
                    if (key != null) {
                        message.append(ByteUtil.byteArrayToLeInt(key));
                        message.append(" ");
                    }
                    message.append(Arrays.toString(key));
                    message.append("\n");

                    message.append("\n");
                }
                intent.putExtra(Intent.EXTRA_TEXT, message.toString());
            }

            Intent chooser = Intent.createChooser(intent, "Choose Email Application").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(chooser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void toEmailAsRawFile() {
        toEmailAsRawFile(null);
    }

    public static void toEmailAsRawFile(String email) {
        toEmailAsRawFile(email, null);
    }

    // No Decryption
    public static void toEmailAsRawFile(String email, Intent intent) {

        Context context = Royal.getApplicationContext();

        List<File> files = FileUtil.getFilesFrom(context.getFilesDir(), ".realm");

        // Exporting .realm files into cache directory
        ArrayList<Uri> uris = new ArrayList<>();
        for (File file : files) {
            File realmFile = new File(context.getExternalCacheDir(), file.getName());
            FileUtil.copy(file, realmFile);
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

            for (File file : files) {
                message.append("Name: ");
                message.append(file.getName());
                message.append("\n");

                message.append("Path: ");
                message.append(file.getPath());
                message.append("\n");

                message.append("\n");
            }
            intent.putExtra(Intent.EXTRA_TEXT, message.toString());
        }

        Intent chooser = Intent.createChooser(intent, "Choose Email Application").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(chooser);
    }

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
    // mnt/sdcard/realms/default.realm
    // Decrypt Automatically
    public static void toExternalStorage(RealmConfiguration... configurations) {
        if (configurations == null || configurations.length == 0)
            return;

        List<Realm> realms = new ArrayList<>();
        for (RealmConfiguration configuration : configurations)
            realms.add(Realm.getInstance(configuration));

        toExternalStorage(realms.toArray(new Realm[realms.size()]));

        for (Realm realm : realms)
            realm.close();
    }

    public static void toExternalStorage(Realm... realms) {
        if (realms == null || realms.length == 0)
            return;

        for (Realm realm : realms) {
            File file = new File(Environment.getExternalStorageDirectory() + "/realms/" + realm.getConfiguration().getRealmFileName());
            try {
                realm.writeCopyTo(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void toExternalStorageAsRawFile() {

        Context context = Royal.getApplicationContext();

        List<File> files = FileUtil.getFilesFrom(context.getFilesDir(), ".realm");

        for (File file : files) {
            File realmFile = new File(Environment.getExternalStorageDirectory() + "/realms/" + file.getName());
            FileUtil.copy(file, realmFile);
        }
    }
}
