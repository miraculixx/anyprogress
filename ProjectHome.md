This project aims to create a progress indicator that can be displayed to users for processes which you don't control.

Currently, the tool implements a file and directory monitor that will display a Swing-JProgressBar and optionally a progress log. The progress indicated is configured by a properties file, which maps file names to current progress.

The vision is to create a generic and extensible toolset to include arbitrary resource monitors (files, URIs, memory, etc.), different progress indicators (Swing, console, Ajax/HMTL etc.) and reporting options.