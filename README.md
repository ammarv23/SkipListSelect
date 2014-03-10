SkipListSelect
==============

CPSC 320 Assignment 5

This is an implementation of a Skip List data structure that also implements a Select method to find the lowest ranked value
in a random set.

In order to test my step by step calculation of ranks as the program runs, set the boolean value "helperPrintVal" in the SkipList.java class to true, this will enable printouts
of the right and left pointer ranks for each node affected by any particular insertion or deletion. As well as a printout of the Skip List at each insertion and deletion for
easy following.

I didn't realize we had to unzip the .jar file to access the files within, so I implemented the SkipList from a tutorial I found online at :

http://www.mathcs.emory.edu/~cheung/Courses/323/Syllabus/Map/skip-list-impl.html#newlayer

I still completed the assignment fully, but I had an extra class to accomodate the printouts, as the tutorial allowed both vertical and horizontal and I liked the ease of use.

Due to the lack of SkipListTest.java, I implemented all my tests directly into the main class of SkipList.java.

I have used randomized values for the inputs as well as the choice of ranks, so duplicating tests might be difficult, but as I feel my implementation is correct, this gives the
best range of tests as it covers all possibilities.
