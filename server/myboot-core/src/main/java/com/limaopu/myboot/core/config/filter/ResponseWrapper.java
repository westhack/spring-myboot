package com.limaopu.myboot.core.config.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * @author mac
 */
public class ResponseWrapper extends HttpServletResponseWrapper {

    private class ResponsePrintWriter extends PrintWriter
    {
        ByteArrayOutputStream output;

        public ResponsePrintWriter(ByteArrayOutputStream output)
        {
            super(output);
            this.output = output;
        }

        public ByteArrayOutputStream getByteArrayOutputStream()
        {
            return output;
        }
    }

    private ResponsePrintWriter writer;
    private ByteArrayOutputStream output;

    public ResponseWrapper(HttpServletResponse httpServletResponse)
    {
        super(httpServletResponse);
        output = new ByteArrayOutputStream();
        writer = new ResponsePrintWriter(output);
    }

    @Override
    public void finalize() throws Throwable
    {
        super.finalize();
        output.close();
        writer.close();
    }

    public String getContent()
    {
        try
        {
            writer.flush();
            return writer.getByteArrayOutputStream().toString("UTF-8");
        }
        catch(UnsupportedEncodingException e)
        {
            return "UnsupportedEncoding";
        }
    }

    public void close() throws IOException
    {
        writer.close();
    }

    @Override
    public PrintWriter getWriter() throws IOException
    {
        return writer;
    }
}
