package OtherAlgo;

import java.util.ArrayList;
import java.util.List;

public class shopee_nowcoder {
    static List<String> result = new ArrayList<>();
    public static void main(String []args){
        int [][] nums = new int[5][5];
        nums[0]=new int[]{0,0,0,1,0};
        nums[1]=new int[]{1,1,0,0,1};
        nums[2]=new int[]{0,0,0,0,0};
        nums[3]=new int[]{1,0,1,0,0};
        nums[4]=new int[]{0,0,0,0,0};
        find(new ArrayList<>(),nums,new boolean[nums.length][nums[0].length],0,0);
        System.out.println(result);
    }
        public static void find(List<String> list ,int[][]nums, boolean[][] memo, int m ,int n )
        {
            if( m < 0 || n < 0 || m >= nums.length || n >= nums[0].length){
                return;
            }

            if(m ==nums.length-1 && n==nums[0].length-1){
                if(result.size()==0||list.size()<result.size()){
                    list.add((nums.length-1)+","+(nums[0].length-1));
                    result=new ArrayList<>(list);
                }
            }
            if(!memo[m][n]&&nums[m][n]!=1){
                memo[m][n]=true; list.add(m+","+n);
                find(list,nums,memo,m+1,n);
                find(list,nums,memo,m-1,n);
                find(list,nums,memo,m,n+1);
                find(list,nums,memo,m,n-1);
                memo[m][n]=false;
                list.remove(list.size()-1);
            }
        }
}
