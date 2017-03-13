#include<stdio.h>
#include<stdlib.h>

void printbill(int*);
void editorder(int*);
int price =20;
void main(){
	int items[3]={0,0,0};
	/*0-> Start state where order is taken from the user; 1-> Where the user csn view his order and proceed to pay; 2-> Where user inputs
		currency into the machine until the final amount is matched; 3-> THis is the final state where the order of the user is accepted 
		and the user is given his order; 4-> The user can chose to edit his order in this stage; 5->Dead state*/
	int states[6]={0,1,2,3,4,5};
	int tot=0,i, c;
	while(1){
		printf("--------State 0---------\n");
		printf("Enter the item you wish to add, or press 4 to proceed to pay, or 5 to cancel transaction\n\n");
		printf("1. Coffee\n");
		printf("2. Tea\n");
		printf("3. Soup\n");
		scanf("%d",&c);
		if(c==4){
			break;
		}
		else if(c==5){
			printf("---------State 5---------\nTerminating...\n");
			exit(0);
		}
		else if(c==1){
			printf("Coffee added to bag\n\n");
			tot=tot+price;
			items[0]++;
		}
		else if(c==2){
			printf("Tea added to bag\n\n");
			tot=tot+price;
			items[1]++;
		}
		else if(c==3){
			printf("Soup added to bag\n\n");
			tot=tot+price;
			items[2]++;
		}
	}
	int paid=0,x;
	printf("________________________________________________________________________________________________________\n\n");
	printbill(items);
	while(1){
		printf("---------State 1---------\n");	
		printf("Please enter your choice:\n1. Proceed to make payment\n2. Edit order\n3. Cancel transaction \nYour choice: ");
		scanf("%d",&x);
		if(x==3){
			printf("\n--------State 5---------\nTerminating...\n");
			exit(0);
		}
		else if(x==2){
			editorder(items);
		}
		else if(x==1){
			break;
		}
	}
	tot=(items[0]+items[1]+items[2])*20;
	printf("Accepted denominations are: \nRs.1\tRs.2\tRs.5\tRs.10\n");
	int input;
	while(paid!=tot){
		printf("State 2\n");
		printf("Drop in money into machine\n");
		scanf("%d",&input);
		switch(input){
			case 1:
				paid=paid+1;
				printf("Recieved Rs.1. Amount remaining=%d\n",(tot-paid));
				break;
			case 2:
				if(paid+input>tot){
					printf("Invalid amount. Exceeding total. Please collect money and try again.\n");
					break;
				}
				paid=paid+2;
				printf("Recieved Rs.2. Amount remaining=%d\n",(tot-paid));
				break;
			case 5:
				if(paid+input>tot){
					printf("Invalid amount. Exceeding total. Please collect money and try again\n");
					break;
				}
				paid=paid+5;
				printf("Recieved Rs.5. Amount remaining=%d\n",(tot-paid));
				break;
			case 10:
				if(paid+input>tot){
					printf("Invalid amount. Exceeding total. PLease collect money and try again\n");
					break;
				}
				paid=paid+10;
				printf("Recieved Rs.10. Amount remaining=%d\n",(tot-paid));
				break;
			default: printf("Invalid currency.\n");
		}
	}
	printf("---------State 3---------\n");
	printf("Thank you. Enjoy!");
	
}
void printbill(int* items){
	int i;
	printf("\n\nYour bill is as follows:\n");
	printf("Item\t\tUnits\t\tAmount\n");
	for(i=0;i<3;i++){
		if(items[i>0]){
			if(i==0){
				printf("Coffee");
			}
			else if(i==1){
				printf("Tea");
			}
			else if(i==2){
				printf("Soup");
			}
			printf("\t\t%d\t\t%d",items[i],items[i]*20);
		}
		printf("\n");
	}
	printf("\nYour billing amount comes up to %d\n",(items[0]+items[1]+items[2])*20);
}
void editorder(int* items){
	int i,c;
	while(1){
		printf("\n---------State 4---------\n");
		printf("Please enter your choice of item you wish to edit or press 4 to view bill and exit:\n 1. Coffee\n2. Tea\n3. Soup\n\nYour choice:");
		scanf("%d",&c);
		if(c==1){
			printf("\nEnter amount of Coffee: ");
			scanf("%d",&i);
			if(i<0){
				printf("\nCant have lesser coffee than 0\n");
			}
			else{
				items[0]=i;
				printf("\nNo. of cofee=%d and price=%d\n",i,i*20);
			}
		}
		else if(c==2){
			printf("\nEnter amount of Tea: ");
			scanf("%d",&i);
			if(i<0){
				printf("\nCant have lesser tea than 0\n");
			}
			else{
				items[1]=i;
				printf("\nNo. of tea=%d and price=%d\n",i,i*20);
			}
		}
		else if(c==3){
			printf("\nEnter amount of Soup: ");
			scanf("%d",&i);
			if(i<0){
				printf("\nCant have lesser soup than 0\n");
			}
			else{
				items[2]=i;
				printf("No. of soup=%d and price=%d\n",i,i*20);
			}
		}
		else if(c==4){
			printbill(items);
			return;
		}
	}
}
