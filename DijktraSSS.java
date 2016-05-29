import java.io.*;
import java.util.Scanner;

public class DijktraSSS 
{
	int numNodes;
	int sourceNode;
	int minNode;
	int currentNode;
	int newCost;
	int [][]costMatrix;
	int []fatherAry;
	int []markedAry;
	int []bestCostAry;
	
	public DijktraSSS(int nodes)
	{
		numNodes = nodes;
		costMatrix = new int[numNodes+1][numNodes+1];
		fatherAry = new int[numNodes+1];
		markedAry = new int[numNodes+1];
		bestCostAry = new int[numNodes+1];
		//allocation of cost matrix
		
		for(int i = 1; i <= numNodes; i++)
		{
			fatherAry[i] = i;   //intialize to fatherAry[i] = i
			markedAry[i] = 0;   //initialize to 0
			bestCostAry[i] = 99999;  //intialize to infinity i.e 99999
		}
		
		//intialization of cost matrix
		for(int j = 1; j <= numNodes; j++)
		{
			for(int k = 1; k <= numNodes; k++)
			{
				if(j == k)
				{
					costMatrix[j][k] = 0;
				}
				else
				{
					costMatrix[j][k] = 99999;
				}	
			}
		}
	}
	
	void loadCostMatrix(Scanner inFile)
	{
		int from, to, cost;
		//while(inFile >> from >> to >> cost)
		while(inFile.hasNext())
		{
			from = inFile.nextInt();
			to = inFile.nextInt();
			cost = inFile.nextInt();
			costMatrix[from][to] = cost;
		}
	}
	
	void loadBestCostAry(int source)
	{
		for(int i = 1; i <= numNodes; i++)
		{
			bestCostAry[i] = costMatrix[source][i];
		}
	}
	
	void loadFatherAry(int source)
	{
		for(int i = 0; i <= numNodes; i++)
		{
			fatherAry[i] = source;	
		}	
	}
	
	void loadMarkedAry()
	{
		for(int i = 0; i <= numNodes; i++)
		{
			markedAry[i] = 0;	
		}	
	}
	
	int computeCost(int minimumNode, int currNode)
	{
		return bestCostAry[minimumNode] +  costMatrix[minimumNode][currNode];		
	} 
	
	void markMinNode(int minimumNode)
	{
		markedAry[minimumNode] = 1;
	} 
	
	void changeFather(int node, int minimumNode)
	{
		fatherAry[node] = minimumNode;
	}
	
	void changeCost(int node, int cost)
	{
		bestCostAry[node] = cost;
	}
	
	void debugPrint(PrintWriter outFile2)
	{
		outFile2.println("The sourceNode is: " + sourceNode);
		outFile2.print("The fatherAry is: ");
		print_1D(fatherAry, outFile2);
		outFile2.print("The bestCostAry is: ");
		print_1D(bestCostAry, outFile2);
		outFile2.print("The markedAry is: ");
		print_1D(markedAry, outFile2);
		outFile2.println();
	}
	
	void printShortestPaths(int current, PrintWriter outFile1)
	{
		int i = 1, count = 1;
		int []temp = new int[numNodes];
		temp[0] = current;
		while(fatherAry[current] != sourceNode)
		{
			temp[i] = fatherAry[current];
			current = fatherAry[current];	
			count++;
			i++;
		}
		
		outFile1.print("The shortest path from " + sourceNode + " to " + temp[0] + " is: " + sourceNode);
		for(int i1 = count - 1; i1 >= 0; i1--)
		{
			outFile1.print("-->" + temp[i1]);
		}					
		outFile1.println("and cost is: " + bestCostAry[temp[0]]);
	}
	
	int findUnmarkedMinNode()
	{
		int result = 999999;
		int cost = 999999;
		for(int i = 1; i <= numNodes; i++)
		{
			if(cost > bestCostAry[i] && markedAry[i] == 0)
			{
				cost = bestCostAry[i];
				result = i;
			}
		}
		return result;
	}
	
	boolean isAllMarked()
	{
		for(int i = 1; i <= numNodes; i++)
		{
			if(markedAry[i] == 0)
			return false;
		}
		return true;
	}
	
	void print_1D(int []ary, PrintWriter outFile2)
	{
		for(int i = 1; i <= numNodes; i++)
		{
			outFile2.print(ary[i] + " ");
		}
		outFile2.println();
	}
	
	void dijktraAlgorith(PrintWriter outFile1, PrintWriter outFile2)
	{
		int count = 1;
		sourceNode = count;
		while(sourceNode <= numNodes)
		{	
			loadBestCostAry(sourceNode);
			loadFatherAry(sourceNode);
			loadMarkedAry();
			minNode = sourceNode;
			markMinNode(minNode);
			debugPrint(outFile2);
			
			while(!isAllMarked())
			{
				minNode = findUnmarkedMinNode();
				markMinNode(minNode);
				debugPrint(outFile2);
				
				for(int i = 1; i <= numNodes; i++)
				{
					if(markedAry[i] == 0)
					{
						currentNode = i;
					}
					else
					{
						continue;	
					}
					newCost = computeCost(minNode, currentNode);
					
					if(newCost < bestCostAry[currentNode])
					{
						changeFather(currentNode, minNode);
						changeCost(currentNode, newCost);
						debugPrint(outFile2);
					}
						
				}
			}
			currentNode = 1;
			outFile1.println("The sourceNode is: " + sourceNode);
			while(currentNode <= numNodes)
			{
				printShortestPaths(currentNode, outFile1);
				currentNode++;
			}
			outFile1.println();
			sourceNode++;
		}

	}



	public static void main(String []argv) throws IOException
	{
		Scanner inFile;
		inFile = new Scanner(new FileReader(argv[0]));
		PrintWriter outFile1 = new PrintWriter(new FileWriter(argv[1]));
		PrintWriter outFile2 = new PrintWriter(new FileWriter(argv[2]));
		
		int item;
		item = inFile.nextInt();
		
		DijktraSSS graph = new DijktraSSS(item);
		graph.loadCostMatrix(inFile);
		graph.dijktraAlgorith(outFile1, outFile2);
		
		inFile.close();
		outFile1.close();
		outFile2.close();
	}

}
