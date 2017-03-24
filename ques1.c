#include<stdio.h>
#include<string.h>
/**
*@ Authors: Samarth Bharti | Adita Kapoor |  Amitesh
*@SNU ID  :   1410110358   |  1410110017  | 1410110044
**/
int numAlpha;	/*Stores the number of alphabets in language*/
 
/*This function finds the column with respect to the input*/ 
int findColumn(char* inputpattern, char* alphabets, int i){
	int j;
	for(j=0;j<numAlpha;j++){
		if(alphabets[j]==inputpattern[i]){
			return j;		//returns the column w.r.t the search alphabet 
		}
	}
	return -1;
}

/*Calculates the next state*/
int State(char *pattern, int patLen, int presentState, int x, char* alphabets)
{
	if (presentState < patLen && findColumn(pattern,alphabets,x) == findColumn(pattern,alphabets,presentState))
	{
  		return presentState+1;
	}
 	int nextState, i; 
  	for (nextState = presentState; nextState > 0; nextState--)
  	{
  		if(pattern[nextState-1] == findColumn(pattern,alphabets,x))
  		{
  			for(i = 0; i < nextState-1; i++)
  			{
  				if (pattern[i] != pattern[presentState-nextState+1+i])
  				break;
  			}
  			if (i == nextState-1)
  			return nextState;
  		}
  	}
 	return 0;
}
 
/*Transition function generator*/
void Transition(char *pattern, int patLen, int transFunc[][numAlpha], char* alphabets)
{
  int presentState, x;
  for (presentState = 0; presentState <= patLen; ++presentState){
  	for (x = 0; x < numAlpha; ++x){
		int column=findColumn(pattern,alphabets,x);
		if(column==-1){
			printf("\nPattern has illegal character\n");
			exit(0);
		}
		//Store corresponding state vs input in the transition table
  		transFunc[presentState][column] = State(pattern, patLen, presentState, x,alphabets);
	}
  }
}

/*This function checks if the pattern exists in the input strings*/
void check(char* pattern, char* input, char* alphabets)
{
  	int patLen = strlen(pattern);
	
  	int N = strlen(input);
  	int transFunc[patLen+1][numAlpha];
	//Calling function to generate relevant transition function
  	Transition(pattern, patLen, transFunc, alphabets);
  	int i,j;
	//Print the transition table
  	printf("The transition table is as follows:\n");
	printf("    ");
	for(i=0;i<numAlpha;i++){
		printf("%c\t",alphabets[i]);
	}
	printf("\n");
	for(i=0;i<patLen;i++){
		printf("Q%d ",i);
		for(j=0;j<numAlpha;j++){
			printf("%d\t",transFunc[i][j]);
		}
		printf("\n");
	}

  	int presentState=0;
	int flag=0;
  	for (i = 0; i < N; i++)
  	{
  		int column=findColumn(input, alphabets,i);
		if(column==-1){
			printf("\nInput has illegal character\n");
			exit(0);
		}
  		presentState = transFunc[presentState][column];
		//Pattern found
  		if (presentState == patLen)
  		{
  			flag=1;
			break;
  		}
  	}
	if(flag==1){
		printf ("\n Pattern found at index %d \n", i-patLen+1);
	}
	else if(flag==0){
  		printf ("\n Pattern not found \n");
	}
}
 

int main()
{
  	printf("\nEnter the number of alphabets in the language: ");
  	scanf("%d",&numAlpha);

  	char alphabets[numAlpha];
  	printf("\nEnter the alphabets in sequence like a string: ");
  	scanf("%s",&alphabets);  

  	char pattern[100];	//stores the pattern
  	char input[100];	//Stores the input string
  	printf("\nEnter the pattern: ");
  	scanf("%s",&pattern);
	printf("\nEnter the input string: ");
	scanf("%s",&input);
	  
	check(pattern, input, alphabets);		//Checks if pattern exists in input
	return 0;
}
