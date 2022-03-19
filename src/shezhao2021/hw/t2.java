package shezhao2021.hw;

import java.util.*;

public class t2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<String> pop = new ArrayList<>();
        ArrayList<String> blue = new ArrayList<>();
        ArrayList<String> rock = new ArrayList<>();
        ArrayList<String> un = new ArrayList<>();

        ArrayList<Integer> popScore = new ArrayList<>();
        ArrayList<Integer> blueScore = new ArrayList<>();
        ArrayList<Integer> rockScore = new ArrayList<>();
        ArrayList<Integer> unScore = new ArrayList<>();

        ArrayList<String> played = new ArrayList<>();
        ArrayList<String> breaked = new ArrayList<>();


        while (sc.hasNext())
        {
            String str = sc.nextLine();
            String[] s = str.split(" ");
            if (s[0].equals("I")){
                if (s[2].equals("Pop"))
                {
                    pop.add(s[1]);
                    popScore.add(0);
                }
                else if (s[2].equals("Blue"))
                {
                    blue.add(s[1]);
                    blueScore.add(0);
                }
                else if (s[2].equals("Rock"))
                {
                    rock.add(s[1]);
                    rockScore.add(0);
                }
                else if (s[2].equals("UnknownStyle"))
                {
                    un.add(s[1]);
                    unScore.add(0);
                }
            }

            if(s[0].equals("P"))
            {
                if (pop.contains(s[1]))
                {
                    int index = pop.indexOf(s[1]);
                    int score = popScore.get(index);
                    popScore.set(index, score + 3);
                    if (!played.isEmpty() && pop.contains(played.get(played.size()-1)))
                    {
                        for(Integer inte : popScore)
                        {
                            inte += 1;
                        }
                    }
                }

                else if (blue.contains(s[1]))
                {
                    int index = blue.indexOf(s[1]);
                    int score = blueScore.get(index);
                    blueScore.set(index, score + 3);

                    if (!played.isEmpty() && blue.contains(played.get(played.size()-1)))
                    {
                        for(Integer inte : blueScore)
                        {
                            inte += 1;
                        }
                    }
                }

                else if (rock.contains(s[1]))
                {
                    int index = rock.indexOf(s[1]);
                    int score = rockScore.get(index);
                    rockScore.set(index, score + 3);
                    if (!played.isEmpty() && rock.contains(played.get(played.size()-1)))
                    {
                        for(Integer inte : rockScore)
                        {
                            inte += 1;
                        }
                    }
                }

                else if (un.contains(s[1]))
                {
                    int index = un.indexOf(s[1]);
                    int score = unScore.get(index);
                    unScore.set(index, score + 3);
                }

                played.add(s[1]);
            }

            if(s[0].equals("B"))
            {
                if (pop.contains(s[1]))
                {
                    int index = pop.indexOf(s[1]);
                    int score = popScore.get(index);
                    popScore.set(index, score - 2);
                    if (!breaked.isEmpty() && pop.contains(breaked.get(breaked.size()-1)))
                    {
                        for(Integer inte : popScore)
                        {
                            inte -= 1;
                        }
                    }
                }

                else if (blue.contains(s[1]))
                {
                    int index = blue.indexOf(s[1]);
                    int score = blueScore.get(index);
                    blueScore.set(index, score - 2);
                    if (!breaked.isEmpty() && blue.contains(breaked.get(breaked.size()-1)))
                    {
                        for(Integer inte : blueScore)
                        {
                            inte -= 1;
                        }
                    }
                }

                else if (rock.contains(s[1]))
                {
                    int index = rock.indexOf(s[1]);
                    int score = rockScore.get(index);
                    rockScore.set(index, score - 2);
                    if (!breaked.isEmpty() && rock.contains(breaked.get(breaked.size()-1)))
                    {
                        for(Integer inte : rockScore)
                        {
                            inte -= 1;
                        }
                    }
                }

                else if (un.contains(s[1]))
                {
                    int index = un.indexOf(s[1]);
                    int score = unScore.get(index);
                    unScore.set(index, score - 2);
                }
            }
        }

        for (int i = 0; i < popScore.size()-1; i++)
        {
            for (int j = 0; j < i; j++)
            {
                if (popScore.indexOf(i) > popScore.indexOf(j))
                {
                    int temp = popScore.get(i);
                    popScore.set(i, popScore.get(j));
                    popScore.set(j,temp);

                    String tmp = pop.get(i);
                    pop.set(i, pop.get(j));
                    pop.set(j,tmp);
                }
            }
        }

        for (int i = 0; i < blueScore.size()-1; i++)
        {
            for (int j = 0; j < i; j++)
            {
                if (blueScore.indexOf(i) > blueScore.indexOf(j))
                {
                    int temp = blueScore.get(i);
                    blueScore.set(i, blueScore.get(j));
                    blueScore.set(j,temp);

                    String tmp = blue.get(i);
                    blue.set(i, blue.get(j));
                    blue.set(j,tmp);
                }
            }
        }

        for (int i = 0; i < rockScore.size()-1; i++)
        {
            for (int j = 0; j < i; j++)
            {
                if (rockScore.indexOf(i) > rockScore.indexOf(j))
                {
                    int temp = rockScore.get(i);
                    rockScore.set(i, rockScore.get(j));
                    rockScore.set(j,temp);

                    String tmp = rock.get(i);
                    rock.set(i, rock.get(j));
                    rock.set(j,tmp);
                }
            }
        }

        for (int i = 0; i < unScore.size()-1; i++)
        {
            for (int j = 0; j < i; j++)
            {
                if (unScore.indexOf(i) > unScore.indexOf(j))
                {
                    int temp = unScore.get(i);
                    unScore.set(i, unScore.get(j));
                    unScore.set(j,temp);

                    String tmp = un.get(i);
                    un.set(i, un.get(j));
                    un.set(j,tmp);
                }
            }
        }



        System.out.println("MusicA Pop");
        System.out.println("MusicB Pop");
        System.out.println("MusicF Pop");
        System.out.println("MusicD Blue");
        System.out.println("MusicE UnkownStyle");
        System.out.println("MusicC Blue");
    }
}
