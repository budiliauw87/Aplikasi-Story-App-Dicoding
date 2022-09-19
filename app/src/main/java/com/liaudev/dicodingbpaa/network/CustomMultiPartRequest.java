package com.liaudev.dicodingbpaa.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Budiliauw87 on 2022-05-19.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
public class CustomMultiPartRequest extends Request<JSONObject> {

    private final Response.Listener<JSONObject> listener;
    private final Map<String, String> headers;
    private final List<MultiPart> parts = new ArrayList<MultiPart>();

    private final String boundary = Long.toHexString(System.currentTimeMillis());

    public CustomMultiPartRequest(String url, Map<String, String> headers,
                                  Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = reponseListener;
        this.headers = headers;
    }

    public CustomMultiPartRequest(int method, String url, Map<String, String> headers,
                                  Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.headers = headers;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        listener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return (headers != null) ? headers : super.getHeaders();
    }

    public void addPart(MultiPart multiPart) {
        if (multiPart != null) {
            parts.add(multiPart);
        }
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + boundary;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            for (MultiPart part: parts) {
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                if (part instanceof FormPart) {
                    dos.writeBytes("Content-Disposition: form-data; name=\"" + part.getName() + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.write(part.getData());
                    dos.writeBytes(lineEnd);
                } else if (part instanceof FilePart) {
                    FilePart filePart = (FilePart) part;
                    dos.writeBytes("Content-Disposition: form-data; name=\"" + part.getName()
                            + "\"; filename=\"" + filePart.getFilename() + "\"" + lineEnd);
                    dos.writeBytes("Content-type: " + filePart.getMimeType() + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.write(part.getData());
                    dos.writeBytes(lineEnd);
                }
            }

            //close out
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    protected static abstract class MultiPart {

        private final String name;
        private final String mimeType;

        public MultiPart(String name, String mimeType) {
            this.name = name;
            this.mimeType = mimeType;
        }

        public String getName() {
            return name;
        }

        public String getMimeType() {
            return mimeType;
        }

        public abstract byte[] getData();
    }

    /**
     * A class to represent a basic form field to be added to the request
     */
    public static class FormPart extends MultiPart {

        private final String value;

        /**
         * Creates a form part with the supplied name and value
         * @param name form field name
         * @param value form field value
         */
        public FormPart(String name, String value) {
            super(name, "");
            this.value = value;
        }

        @Override
        public byte[] getData() {
            return value.getBytes();
        }
    }

    /**
     * A class representing a file to be added to the request
     */
    public static class FilePart extends MultiPart {

        private final byte[] data;
        private final String filename;

        /**
         * Creates a file with the given values to add to the request
         * @param name form field name
         * @param mimeType mime type for part
         * @param filename filename (can be null)
         * @param data the content of the file
         */
        public FilePart(String name, String mimeType, String filename, byte[] data){
            super(name, mimeType);
            this.data = data;
            this.filename = filename;
        }

        public byte[] getData() {
            return data;
        }

        public String getFilename() {
            return filename;
        }
    }

}
