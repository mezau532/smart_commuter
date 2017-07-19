package LyftUber;

import LyftUber.LyftClientCredentials;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
/**
 * Created by umeza on 7/18/17.
 */

public class LyftCalls {
    //TODO at some point I want to take these out of the code
    //I don't know how I will do it right now because we need
    //the clientid and secrete to make lyft api calls
    public String ClientId = "R7K9RlJA-H87";
    public String ClientSecret = "rZnj1OvXQxgk8eCHoCp7owAHCjPIWqhZ";
    public String ClientToken = "roVZU6oVJyhdGGoM/VFKhmyuTmOYvBalKiezPB5PiHiTqsB72/1chvNJ/Zdx/YgvDdKfKiOGSMNBLJbKaXVOyNfj/2cWqAbDzz9gfRh8pA9Av/n0YyUCHbs=";
    public LyftClientCredentials Creds = new LyftClientCredentials();

    public void setCreds() {

    }

    public LyftClientCredentials getCreds() {
        return this.Creds;
    }
}
