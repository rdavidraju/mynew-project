package com.nspl.app.web.rest.util;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

public class ByteArrayDataSource implements DataSource {
    byte[] bytes;
    String contentType;
    String name;

    public ByteArrayDataSource( String name, String contentType, byte[] bytes ) {
      this.name = name;
      this.bytes = bytes;
      this.contentType = contentType;
    }

    public String getContentType() {
      return contentType;
    }

    public InputStream getInputStream() {
      return new ByteArrayInputStream(bytes);
    }

    public String getName() {
      return name;
    }

    public OutputStream getOutputStream() throws IOException {
      throw new FileNotFoundException();
    }
  }