public class Levenshtein {

    public static int minimalDistance(String first, String second) {

        int m = first.length()+1;
        int n = second.length()+1;
        int[][] distanceMatrix = new int[m][n];


        for(int i=0; i<m; i++){
           distanceMatrix[i][0] =i;
        }
        for(int j=1; j<n; j++){
            distanceMatrix[0][j] =j;
        }

        for(int i=1; i<m; i++){
            for(int j=1; j<n; j++){
                int cost = 1;
                if(first.charAt(i-1)==second.charAt(j-1)) cost =0;

                distanceMatrix[i][j] = Math.min(distanceMatrix[i-1][j]+1,
                        Math.min(distanceMatrix[i][j-1]+1, distanceMatrix[i-1][j-1]+cost));
            }
        }


        return distanceMatrix[m-1][n-1];
    }
}
