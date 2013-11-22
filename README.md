Introduction
============

This project is in the phase of forming. It contains the source code from OpenbravoPOS Version 2.30.2 
with some minor modifications. The source code has been spitted into separate modules and the build system
has been moved from ant to maven. If you are familiar with maven you can jump right in:

  mvn package

This will build the project and create a jar file with all dependencies in the 
openpos-launcher/target directory. You can launch this file with:

  java –jar openpos-launcher-2.4-SNAPSHOT-jar-with-dependencies.jar

Ideas
============

Here are some ideas for the project:

 * add NFC Support for NFC payments, NFC gift cards and NFC employee tokens for time recording and access control (see nfctools for java link)
 * add Android based UI ordering system using Android Tablets and Smartphones
 * create a Plugin interface for easy extendibility without having to modify the code base
 * create a Web interface to be able to manage the POS. Keep only cashier relevant staff as Swing based app. 

