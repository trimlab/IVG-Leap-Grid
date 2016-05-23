# IVG Leap Grid
The IVG Leap Grid is a program the uses user-defined blocks to test the usability of a gesture controlled infotainment system.

## Requirements
  - [Leap SDK]
  - [Eclipse]
  - [FreeTTS]

## Configuration Files

### app.cfg (global app settings)
Global configuration variables are as follows:
```
#Leap controller settings
#x is left-right, y is up-down, z is forward-back
max-x = 100
min-x = -100
max-y = 300
min-y = 0
max-z = 80
min-z = -80

#display settings
window-title = Tri-M IVG
start-fullscreen = true
window-width = 800
window-height = 800

#voice settings
voice = kevin16
rate = 130
pitch = 1.2
#delay of voice in milliseconds
delay = 500
```
### Blocks
New blocks can be added by creating a new .cfg file in the `blocks/` directory.
```
#The name is used for display only
name = Default Block
#Block files (One file can be used with multiple blocks)
menu = default.cfg
task = default.cfg

#Show back/home buttons
show-header = false
#If a leaf node is clicked, should I return to the home menu?
return-home-on-leaf = true

#Vocalize hovering
use-speech = true
#Use custom speech files
use-speech-files = true
```

### Menus
Menu .cfg files can be defined for each block. These go in the `menus/` directory.A small menu can be defined as follows:
```
"Main"
	#1x2#
	"Food"
		#1x3#
		"Fruits"
			#3x2#
			"Apple"
			"Orange"
			"Banana"
			"Kiwi"
			"Pineapple"
			"Raspberry"
		"Vegetables"
			#2x2#
			"Tomato"
			"Carrot"
			"Lettuce"
			"Broccoli"
		"Candy"
			#2x3#
			"Sucker"
			"Gum"
			"M&Ms"
			"Chocolate Bar"
			"Skittles"
			"Smarties"
	"Animals"
		#2x1#
		"Land"
			#2x2#
			"Dog"
			"Cat"
			"Bird"
			"Squirrel"
		"Water"
			#3x2#
			"Fish"
			"Dolphin"
			"Whale"
			"Shark"
			"Jellyfish"
			"Octopus"
```
These menus *must be formatted correctly* (indentation is essential). In this example, the `Main` menu will be a 1 row with 2 columns, which will contain the buttons `Food` and `Animals`. `Food` will be 1 row by 3 columns, and `Animals` will be 2 rows by 1 column.

### Tasks
Task .cfg files will tell the participant what to do and record data in excel files. Task files are in the `tasks/` directory. A simple tasks file is as follows:
```
wait 8
Raspberry
wait 2
Cat
wait 4
Tomato
```
This task file will wait 8 seconds after starting, and then ask the participant to find the tile `Raspberry`. Once the participant files the tile `Raspberry`, it will wait 2 seconds and ask them to find the tile `Cat`, and so on. The names for each task *must be the same* as that in the menu file, otherwise the task will never finish because the participant won't be able to find it.

### Sounds
Custom .wav files can be added in the `sounds/` folder. If a file has the same name as a tile (excluding the .wav extension), it will be played in place of the text-to-speech.

## Test Process
1. Open Eclipse
2. Click the Run button
3. Enter the name of the test
   - A name cannot already exist and cannot be blank
4. Click on the name of the block you would like to test
5. Once the test is complete, close the window.
6. If you would like to run another block, go to step 4.
7. Otherwise, close the Block window.
   - Window will close automatically if there are no blocks left
8. Results can be found in `output/` under the name you typed in step 3.


   [Leap SDK]: <https://developer.leapmotion.com/get-started>
   [Eclipse]: <http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/mars2>
   [FreeTTS]: <http://freetts.sourceforge.net/docs/index.php#download_and_install>
