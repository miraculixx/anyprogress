Install Progress Monitor
(c) 2008 Patrick Senti patrick.senti at mygoodlife.ch
Licensed under GNU GPL V3

Purpose

This utility provides a general-purpose installation progress monitor for
applications that do not provide one themselves. The progress reported is
easily configurable by names of directories or files, and no change is
required to the installation procedure itself.

Usage

First, create a configuration file (ASCII text format) where you list all files or 
directories expected to be created, modified or removed. One file or directory
per line using the following syntax:

file=pct;text

where file is an absolute or relative path of the file or directory expected
           to be created, modified or deleted by the installation process
      pct  is the releative progress in percent (values 0..100)
      ;    is a mandatory seperator
      text is the text to be displayed at reaching this progress level
      
List as many files and directories as you want progress to be reported on.
To ensure the progress indicator will exit automatically, list an exit condition
as follows:

file=..exit

This will tell the progress indicator to exit one this file is modified, created
or removed. It may be convenient to use a temporary directory created by the
installation process but removed once it exits. You may also list multiple 
exit commands of this form.

Second, invoke jpi as follows:

java -jar jpi.jar config.jpi

where config.jpi is the name of the configuration file created in the first step

Third, run your installation process.

Whenever a file or directory listed in the configuration file as specified above
is created, modified or removed, the progress indicator will be updated to reflect
the respective percentage and display the text as given in the configuration file.


Advanced options

The configuration file may contain other options and commands:

..log=yes    tells the progress indicator to display a log that is updated with 
             all text items of any progress level reached
..size=HxW   sets the dimensions of the dialog box in pixels (width x height)
             example: 50x100 sets the size to be 50 pixels in height and 100 in width
..init=text  sets the initial text upon start
..title=text sets the title of the dialog box. default is "InstallProgress";
