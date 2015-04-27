package screen.dimmer.pixelfilter;

public class Grids {

    public static final int GridSize = 64;
    public static final int GridDimensions = 8;

    public static int Id[] = new int[] {
            R.id.checkBox1 ,R.id.checkBox2, R.id.checkBox3, R.id.checkBox4, R.id.checkBox5, R.id.checkBox6, R.id.checkBox7, R.id.checkBox8,
            R.id.checkBox9, R.id.checkBox10,R.id.checkBox11,R.id.checkBox12,R.id.checkBox13,R.id.checkBox14,R.id.checkBox15,R.id.checkBox16,
            R.id.checkBox17,R.id.checkBox18,R.id.checkBox19,R.id.checkBox20,R.id.checkBox21,R.id.checkBox22,R.id.checkBox23,R.id.checkBox24,
            R.id.checkBox25,R.id.checkBox26,R.id.checkBox27,R.id.checkBox28,R.id.checkBox29,R.id.checkBox30,R.id.checkBox31,R.id.checkBox32,
            R.id.checkBox33,R.id.checkBox34,R.id.checkBox35,R.id.checkBox36,R.id.checkBox37,R.id.checkBox38,R.id.checkBox39,R.id.checkBox40,
            R.id.checkBox41,R.id.checkBox42,R.id.checkBox43,R.id.checkBox44,R.id.checkBox45,R.id.checkBox46,R.id.checkBox47,R.id.checkBox48,
            R.id.checkBox49,R.id.checkBox50,R.id.checkBox51,R.id.checkBox52,R.id.checkBox53,R.id.checkBox54,R.id.checkBox55,R.id.checkBox56,
            R.id.checkBox57,R.id.checkBox58,R.id.checkBox59,R.id.checkBox60,R.id.checkBox61,R.id.checkBox62,R.id.checkBox63,R.id.checkBox64,
    };

    public static String[] PatternNames = new String[]{

            "12%",
            "25%",
            "38%",
            //"33",
            "50%",
            "62%",
            "75%",
            "88%",
    };

    public static byte[][] Patterns = new byte[][] {
            {
                    1, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 1, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 1, 0,
                    0, 1, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 1, 0, 0,
                    0, 0, 1, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 1,
                    0, 0, 0, 0, 1, 0, 0, 0,
            },
            {
                    1, 0, 0, 0, 1, 0, 0, 0,
                    0, 0, 1, 0, 0, 0, 1, 0,
                    0, 1, 0, 0, 0, 1, 0, 0,
                    0, 0, 0, 1, 0, 0, 0, 1,
                    1, 0, 0, 0, 1, 0, 0, 0,
                    0, 0, 1, 0, 0, 0, 1, 0,
                    0, 1, 0, 0, 0, 1, 0, 0,
                    0, 0, 0, 1, 0, 0, 0, 1,
            },
/*
            {
                    1, 0, 0, 1, 0, 0,
                    0, 0, 1, 0, 0, 1,
                    0, 1, 0, 0, 1, 0,
                    0, 0, 1, 0, 0, 1,
                    1, 0, 0, 1, 0, 0,
                    0, 1, 0, 0, 1, 0,
            },
*/
            {
                    1, 0, 0, 0, 1, 0, 0, 0,
                    0, 1, 0, 1, 0, 1, 0, 1,
                    0, 0, 1, 0, 0, 0, 1, 0,
                    0, 1, 0, 1, 0, 1, 0, 1,
                    1, 0, 0, 0, 1, 0, 0, 0,
                    0, 1, 0, 1, 0, 1, 0, 1,
                    0, 0, 1, 0, 0, 0, 1, 0,
                    0, 1, 0, 1, 0, 1, 0, 1,
            },
            {
                    1, 0, 1, 0, 1, 0, 1, 0,
                    0, 1, 0, 1, 0, 1, 0, 1,
                    1, 0, 1, 0, 1, 0, 1, 0,
                    0, 1, 0, 1, 0, 1, 0, 1,
                    1, 0, 1, 0, 1, 0, 1, 0,
                    0, 1, 0, 1, 0, 1, 0, 1,
                    1, 0, 1, 0, 1, 0, 1, 0,
                    0, 1, 0, 1, 0, 1, 0, 1,
            },
            {
                    0, 1, 1, 1, 0, 1, 1, 1,
                    1, 0, 1, 0, 1, 0, 1, 0,
                    1, 1, 0, 1, 1, 1, 0, 1,
                    1, 0, 1, 0, 1, 0, 1, 0,
                    0, 1, 1, 1, 0, 1, 1, 1,
                    1, 0, 1, 0, 1, 0, 1, 0,
                    1, 1, 0, 1, 1, 1, 0, 1,
                    1, 0, 1, 0, 1, 0, 1, 0,
            },
            {
                    0, 1, 1, 1, 0, 1, 1, 1,
                    1, 1, 0, 1, 1, 1, 0, 1,
                    1, 0, 1, 1, 1, 0, 1, 1,
                    1, 1, 1, 0, 1, 1, 1, 0,
                    0, 1, 1, 1, 0, 1, 1, 1,
                    1, 1, 0, 1, 1, 1, 0, 1,
                    1, 0, 1, 1, 1, 0, 1, 1,
                    1, 1, 1, 0, 1, 1, 1, 0,
            },
            {
                    0, 1, 1, 1, 1, 1, 1, 1,
                    1, 1, 1, 0, 1, 1, 1, 1,
                    1, 1, 1, 1, 1, 1, 0, 1,
                    1, 0, 1, 1, 1, 1, 1, 1,
                    1, 1, 1, 1, 1, 0, 1, 1,
                    1, 1, 0, 1, 1, 1, 1, 1,
                    1, 1, 1, 1, 1, 1, 1, 0,
                    1, 1, 1, 1, 0, 1, 1, 1,
            },
    };

}
