# ImprovedAssignmentsProblem
The Job Assignment problem: There are n people who need to be assigned to execute
n jobs, one person per job. (That is, each person is assigned to exactly one job and each job is assigned to exactly one person.) The productivity gain that would be attained if the  ith  person is assigned to the jth job is a known quantity C[i, j] for each pair i, j=0,2,...,n-1. The problem is to find an assignment with the maximum total productivity gain.
 
Design, implement and thoroughly test your own solution for any user specified value of n and any user specified matrix C[i,j]. Use Best-First Branch&Bound as discussed in class and textbook pages 433-436 for reference.
 
In addition your solution should be designed according to the specifications below:
·         Use Best-First Branch&Bound discussed in class
·         the solution should work for any user specified input using the format specified in the input files below
·         the solution should be iterative (i.e. non-recursive)
·         the solution should display the best job assignment using the format specified in Assignment 2
·         the solution should count and display the number of  states evaluated = sum( number of complete job assignment states explored , number of partial job assignment states explored where the search was pruned).
Display:
a) number of complete job assignment states explored 
b) number of partial job assignment states explored
c) total = sum of a) and b)
