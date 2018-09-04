package jnr_ffi;

import jnr.ffi.LibraryLoader;

public class HelloWorld {
	 public static interface LibC {  
         int puts(String s);  
     }  
   
     public static void main(String[] args) {  
         LibC libc = LibraryLoader.create(LibC.class).load("msvcrt");  
         libc.puts("Hello, World");  
         com.datastax.driver.core.Native a;
     }  
}
