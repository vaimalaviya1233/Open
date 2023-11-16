package com.vaibhav.open;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tananaev.adblib.AdbBase64;
import com.tananaev.adblib.AdbConnection;
import com.tananaev.adblib.AdbCrypto;
import com.tananaev.adblib.AdbStream;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class DrAdb {
    public static final String PUBLIC_KEY_NAME = "public.key";
    public static final String PRIVATE_KEY_NAME = "private.key";
    public static String TAG = "Dr.Adb Said : ";
    public static String TAGG = "Informer Said : ";
    private static Context context;
    private AdbConnection connector = null;
    private Socket socket = null;
    private AdbCrypto crypto = setupCrypto();
//    private final Context mcontext;
//    private final String adbCommand;

    public DrAdb(Context context) {
        DrAdb.context = context;
//        this.mcontext = context;
//        adbCommand = cmd;
    }

    private static AdbBase64 getBase64Impl() {
        return Base64::encodeBase64String;
    }

    @Nullable
    private static AdbCrypto setupCrypto() {
        File dataDir = context.getDataDir();
        File pub = new File(dataDir + "//" + PUBLIC_KEY_NAME);
        File priv = new File(dataDir + "//" + PRIVATE_KEY_NAME);
        AdbCrypto crypto = null;

        if (pub.exists() && priv.exists()) {
            try {
                crypto = AdbCrypto.loadAdbKeyPair(DrAdb.getBase64Impl(), priv, pub);
            } catch (IOException e) {
                System.out.println(TAG + "IOException at line 48");
                crypto = null;
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                System.out.println(TAG + "NoSuchAlgorithmException at line 48");
                e.printStackTrace();
                crypto = null;
            } catch (InvalidKeySpecException e) {
                System.out.println(TAG + "InvalidKeySpecException at line 48");
                e.printStackTrace();
                crypto = null;
            }
        }
        if (crypto == null) {
            try {
                crypto = AdbCrypto.generateAdbKeyPair(DrAdb.getBase64Impl());
                crypto.saveAdbKeyPair(priv, pub);
                System.out.println(TAGG + "Generated new KeyPair");
            } catch (NoSuchAlgorithmException | IOException e) {
                System.out.println(TAG + "Genaration//save Error at line 66/67");
                e.printStackTrace();
            }
        } else {
            System.out.println(TAGG + "Loaded Existing Keypair");
        }
        return crypto;
    }

    public static String getdeviceIpAddress() {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        Log.d(TAG, String.valueOf(ip));
        System.out.println(" IP Address is " + ip);
        return ip;
    }

    public String Commander(String cmd) {
        String[] Result = new String[10];
        crypto = setupCrypto();
        System.out.println(TAGG + "Socket Connecting...");
        try {
            socket = new Socket(getdeviceIpAddress(), 5555);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println(TAG + "UnknownhostException at line 83");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(TAG + "IOException at line 83");
        }
        System.out.println(TAGG + "Socket Connected");
        assert socket != null;
        try {
            connector = AdbConnection.create(socket, crypto);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(TAG + "Error at line 96 & Thrown IOException");
        }
        System.out.println(TAGG + "ADB Connecting....");
        assert connector != null;
        try {
            connector.connect();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println(TAG + "Error at line 104");
        }
        System.out.println(TAGG + "ADB Connected");

        AdbStream stream = null;
        try {
            stream = connector.open("shell:" + cmd);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println(TAG + "Error at line 114 by InterruptedException");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println(TAG + "Error at line 114 by UnsupportedEncodingException");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(TAG + "Error at line 114 by IOException");
        }

        AdbStream finalStream = stream;
        StringBuilder h = new StringBuilder();

        while (!finalStream.isClosed()) {
            try {
                try {
                    String[] Temp = new String[100];
                    h.append(new String(finalStream.read(), StandardCharsets.US_ASCII));
                    System.out.println(new String(finalStream.read(), StandardCharsets.US_ASCII));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println(TAG + "Error At line 132 by InterruptedException");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(TAG + "Error At line 132 by IOException");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Unknown Exception Thrown Between lines 130 to 141");
            }
        }
        return h.toString();
    }
}
