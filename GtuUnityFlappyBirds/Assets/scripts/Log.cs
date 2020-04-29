using System;
using System.Diagnostics;
using System.Reflection;
using System.Text.RegularExpressions;

public class Log {

    private static Log Instance = new Log ();
    private static Regex CLASS_NAME_REGEX = new Regex (@".*?(?<clzName>\w+)\.cs", RegexOptions.Compiled | RegexOptions.IgnoreCase);

    void showPublicMethods (System.Object obj) {
        Console.WriteLine ("-----------------------------------------------");
        Type myType = obj.GetType (); //(typeof(obj));
        MethodInfo[] mths = myType.GetMethods (BindingFlags.Public | BindingFlags.Instance | BindingFlags.DeclaredOnly);
        Console.WriteLine ("Object Info : " + myType);
        for (int ii = 0; ii < mths.Length; ii++) {
            Console.WriteLine (ii + "\t" + mths[ii]);
        }
        Console.WriteLine ("-----------------------------------------------");
    }

    String getClassName (String fileName) {
        if (fileName != null) {
            MatchCollection matches = CLASS_NAME_REGEX.Matches (fileName);
            foreach (Match match in matches) {
                GroupCollection groups = match.Groups;
                return groups["clzName"].Value;
            }
        }
        return "<NA>";
    }

    public static void debug (System.Object message) {
        StackTrace st = new StackTrace (true);
        if (st.FrameCount > 0) {
            int i = st.FrameCount - 1;
            StackFrame sf = st.GetFrame (i);
            int lineNumber = sf.GetFileLineNumber ();
            String className2 = Instance.getClassName (sf.GetFileName ());
            String methodName2 = sf.GetMethod ().Name;
            Console.WriteLine (className2 + "." + methodName2 + "(" + lineNumber + ") : " + message);
        }
    }

    void simple_in_class_debug (System.Object message) {
        System.String className = this.GetType ().Name;
        System.String methodName = MethodBase.GetCurrentMethod ().Name;
        System.Console.WriteLine ("#" + className + "." + methodName + "()------------" + message);
    }
}