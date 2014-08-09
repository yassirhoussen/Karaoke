Karaoke
=======

This a simple karaoke player, write in Java. This project is a very old project, I did it few years ago,
when i was in bachelor degree at Université Paris-Descartes with some friends.

You may have an error like java.lang.OutOfMemoryError, so you have to increase your VM heap space. 
2 possibilities to resolve this problem : 

1- In the Eclipse download folder make the entries in the eclipse.ini file :
--launcher.XXMaxPermSize 
512M
-vmargs
-Dosgi.requiredJavaVersion=1.5
-Xms512m
-Xmx2048m

2 - Or in Eclipse, Run->Run Configuration find the Name of the class you have been running,
select it, click the Arguments tab then add:
-Xms512M -Xmx2048M to the VM Arguments section

source : http://stackoverflow.com/questions/8600972/increasing-heap-space-in-eclipse-java-lang-outofmemoryerror

project credits : 
- Alper Baskan
- Vijay Sekar
- Katrina Gigante
- Yassir Houssen Abdullah
