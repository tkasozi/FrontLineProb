package frontline;

/**
 *
 * Frontline challenge
 *
 * @author tkasozi <talik.aziizi@gmail.com>
 *
 * Talik Kasozi
 *
 * (781)300-1440
 *
 * February 3, 2017
 *
 * Problem: Give a String "(id,created,employee(id,first name,employeeType(id),
 * lastname),location)"
 *
 * display output: id created employee - id - firstname - employeeType -- id -
 * lastname location
 *
 * Bonus output (alphabetic order): created employee - employeeType -- id -
 * firstname - id - lastname id location
 *
 */
class Entity {

    private String name = "";
    private int level = 0;
    private Entity relation = null;

    public Entity() {

    }

    public Entity(String n, int l) {
        name = n;
        level = l;
    }

    public void setRelation(String n, int l) {
        relation = new Entity(n, l);
    }

    public Entity getRelation() {
        return relation;
    }

    public boolean hasRelation() {
        return null != relation;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

}

class FrontLineComparator implements java.util.Comparator<Entity> {

    @Override
    public int compare(Entity o1, Entity o2) {
        if (new Integer(o1.getLevel()).compareTo(o2.getLevel()) == 0) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        } else {
            return 1;
        }
    }

}

class Solution {

    java.util.List<String> list = new java.util.ArrayList<>();
    String[] args;

    String result = "";
    String problemName = "";

    public String getResult() {
        return result;
    }

    public Solution(String arg) {
        arg = arg.replace(' ', '#');
        args = arg.split("");

        for (int i = 0; i < args.length - 1; i++) {
            list.add(args[i]);
        }
    }

    /**
     * The name of the problem to execute. ie. problem1, and bonus
     *
     * @param problem
     */
    public void exe(String problemName) {
        this.problemName = problemName;
        
        switch (this.problemName) {
            case "problem1":
                result = Tokenizer(this.list, 0, "", -1, false);
                break;
            case "bonus":
                result = sortedTokenizer(Tokenizer(this.list, 0, "", -1, false));
                break;
            default:
        }
    }

    public String sortedTokenizer(String str) {
        String[] tempArgs = str.split("\\s");
        String tempResult = "";
        java.util.List<Entity> tespList = new java.util.ArrayList<>();

        for (int i = 0; i < tempArgs.length; i++) {
            String entityString = tempArgs[i];

            Entity entity;

            if (!entityString.isEmpty()) {
                if (!entityString.contains("-")) {

                    entity = new Entity();
                    entity.setName(entityString);

                } else {
                    String peek = tempArgs[i + 1];
                    int level = entityLevel(entityString);
                    entity = new Entity();

                    entity.setLevel(level);
                    entity.setName(entityString);

                    if (peek != null && entityLevel(peek) > level) {
                        entity.setRelation(peek, entityLevel(peek));
                        ++i; //skip
                    }
                }
                tespList.add(entity);
            }
        }

        java.util.Collections.sort(tespList, new FrontLineComparator());

        for (Entity e : tespList) {
            if (e.hasRelation()) {
                Entity rel = e.getRelation();
                tempResult += e.getName() + " " + rel.getName() + " ";
            } else {
                tempResult += e.getName() + " ";
            }
        }

        return tempResult;
    }

    public int entityLevel(String string) {
        int endof = string.lastIndexOf('-');
        String level = string.substring(0, endof + 1);
        return level.length();
    }

    public String Tokenizer(java.util.List args, int index, String result, int level, boolean levelSetFlag) {
        if (index >= args.size()) {
            return result;
        }
        if (args.get(index).equals(")")) {
            level--;
        }
        if (args.get(index).equals("(")) {
            return result + Tokenizer(args, ++index, result, ++level, false);
        } else {
            String levelSymbol = " ";

            for (int i = 0; i < level; i++) {
                levelSymbol += "-";
            }

            if (!args.get(index).equals(")")) {
                String string = ((String) args.get(index));

                if (string.equals(",")) {
                    result += levelSymbol;
                } else if (!levelSetFlag) {
                    result = (levelSymbol + string);
                    levelSetFlag = true;
                } else {
                    switch (string) { //changing symbols
                        case ",":
                            string = " ";
                            break;
                        case "#":
                            string = "";
                            break;
                    }
                    result += string;
                }
            }

            return Tokenizer(args, ++index, result, level, levelSetFlag);
        }

    }

    @Override
    public String toString() {
        String resultString = "******"+this.problemName + "******\n";

        for (String entity : this.result.split("\\s")) {
            if (!entity.isEmpty()) {
                resultString += entity + "\n";
            }
        }
        return resultString;
    }

}

public class FrontLineLexer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String arg = "(id,created,employee(id,first name,employeeType(id), lastname),location)";
        Solution solution = new Solution(arg);

        solution.exe("problem1");

        System.out.println(solution.toString());

        solution.exe("bonus");

        System.out.println(solution.toString());
    }
}
