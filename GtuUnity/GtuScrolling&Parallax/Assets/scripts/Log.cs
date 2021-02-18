using System;
using System.Diagnostics;
using System.Reflection;
using System.Text.RegularExpressions;
using UnityEngine;

public class Log {

    private static Log Instance = new Log ();
    private static Regex CLASS_NAME_REGEX = new Regex (@".*?(?<clzName>\w+)\.cs", RegexOptions.Compiled | RegexOptions.IgnoreCase);

    public static void showPublicMethods (Type obj, System.String label = null) {
        Console.WriteLine ("-----------------------------------------------");
        Type myType = obj; //(typeof(obj));
        MethodInfo[] mths = myType.GetMethods ();//BindingFlags.Public | BindingFlags.Instance | BindingFlags.DeclaredOnly
        Console.WriteLine ("[method] Object Info : " + myType + "\t" + mths.Length);
        for (int ii = 0; ii < mths.Length; ii++) {
            Console.WriteLine (ii + "\t" + mths[ii]);
        }
        Console.WriteLine ("-----------------------------------------------");
    }

    public static void showPublicMethods (System.Object obj, System.String label = null) {
        Console.WriteLine ("-----------------------------------------------");
        Type myType = obj.GetType (); 
        MethodInfo[] mths = myType.GetMethods (BindingFlags.Public | BindingFlags.Instance | BindingFlags.DeclaredOnly);
        Console.WriteLine ("[method] Object Info : " + myType + "\t" + mths.Length);
        for (int ii = 0; ii < mths.Length; ii++) {
            Console.WriteLine (ii + "\t" + mths[ii]);
        }
        Console.WriteLine ("-----------------------------------------------");
    }

    public static void showPublicFields (System.Object obj, System.String label = null) {
        Console.WriteLine ("-----------------------------------------------");
        Type myType = obj.GetType (); //(typeof(obj));
        FieldInfo[] myField = myType.GetFields(BindingFlags.Public | BindingFlags.Static);
        Console.WriteLine ("[field] Object Info : " + myType + "\t" + myField.Length);
        for(int i = 0; i < myField.Length; i++){
            string valueStr = "";
            try{
                valueStr = "value:" + myField[i].GetValue(null);
            }catch(Exception ex) {
            }
            Console.WriteLine (i + "\t" + myField[i].Name + "\t" + myField[i].IsSpecialName + "\t" + valueStr);
        }
        Console.WriteLine ("-----------------------------------------------");
    }

    public static void showPublicFields (Type obj, System.String label = null) {
        Console.WriteLine ("-----------------------------------------------");
        Type myType = obj; //(typeof(obj));
        FieldInfo[] myField = myType.GetFields();//BindingFlags.Public | BindingFlags.Static
        Console.WriteLine ("[field] Object Info : " + myType + "\t" + myField.Length);
        for(int i = 0; i < myField.Length; i++){
            string valueStr = "";
            try{
                valueStr = "value:" + myField[i].GetValue(null);
            }catch(Exception ex) {
            }
            Console.WriteLine (i + "\t" + myField[i].Name + "\t" + myField[i].IsSpecialName + "\t" + valueStr);
        }
        Console.WriteLine ("-----------------------------------------------");
    }

    public static void showPublicProperties (System.Object obj, System.String label = null) {
        Console.WriteLine ("-----------------------------------------------");
        Type myType = obj.GetType (); //(typeof(obj));
        PropertyInfo[] myProps = myType.GetProperties();//BindingFlags.Public | BindingFlags.Static
        Console.WriteLine ("[properties] Object Info : " + myType + "\t" + myProps.Length);
        for(int i = 0; i < myProps.Length; i++){
            string valueStr = "";
            try{
                valueStr = "value:" + myProps[i].GetValue(null, null);
            }catch(Exception ex) {
            }
            Console.WriteLine (i + "\t" + myProps[i].Name + "\t" + myProps[i].IsSpecialName + "\t" + valueStr);
        }
        Console.WriteLine ("-----------------------------------------------");
    }

    public static void showPublicProperties (Type obj, System.String label = null) {
        Console.WriteLine ("-----------------------------------------------");
        Type myType = obj; //(typeof(obj));
        PropertyInfo[] myProps = myType.GetProperties();//BindingFlags.Public | BindingFlags.Static
        Console.WriteLine ("[properties] Object Info : " + myType + "\t" + myProps.Length);
        for(int i = 0; i < myProps.Length; i++){
            string valueStr = "";
            try{
                valueStr = "value:" + myProps[i].GetValue(null, null);
            }catch(Exception ex) {
            }
            Console.WriteLine (i + "\t" + myProps[i].Name + "\t" + myProps[i].IsSpecialName + "\t" + valueStr);
        }
        Console.WriteLine ("-----------------------------------------------");
    }

    /*
    * Log.showAll(typeof(UnityEngine.Debug));
    */
    public static void showAll(Type obj, System.String label = null) {
        showPublicProperties(obj, label);
        showPublicFields(obj, label);
        showPublicMethods(obj, label);
    }

    public static void showAll(System.Object obj, System.String label = null) {
        showPublicProperties(obj, label);
        showPublicFields(obj, label);
        showPublicMethods(obj, label);
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

    public static void error(System.Object message) {
        debug(message, 3);
    }

    public static void warn(System.Object message) {
        debug(message, 2);
    }

    public static void info(System.Object message) {
        debug(message, 1);
    }

    public static void debug (System.Object message) {
        debug(message, 0);
    }

    private static void debug (System.Object message, int showInConsoleLevel) {
        StackTrace st = new StackTrace (true);
        int showPos = -1;
        for(int i = 0 ; i < st.FrameCount; i ++) {
            StackFrame sf = st.GetFrame (i);
            int lineNumber = sf.GetFileLineNumber ();
            String className2 = Instance.getClassName (sf.GetFileName ());
            String methodName2 = sf.GetMethod ().Name;
            if(className2 == typeof(Log).Name) {
                showPos = i + 1;
            }
            //Console.WriteLine (i + "-" + className2 + "." + methodName2 + "(" + lineNumber + ") : " + message);
        }
        if(showPos != -1) {
            int i = showPos;
            StackFrame sf = st.GetFrame (i);
            int lineNumber = sf.GetFileLineNumber ();
            String className2 = Instance.getClassName (sf.GetFileName ());
            String methodName2 = sf.GetMethod ().Name;
            String finalMessage = className2 + "." + methodName2 + "(" + lineNumber + ") : " + message;
            Console.WriteLine (finalMessage);

            switch(showInConsoleLevel) {
                case 0:
                break;
                case 1:
                    UnityEngine.Debug.Log(finalMessage);
                break;
                case 2:
                    UnityEngine.Debug.LogWarning(finalMessage);
                break;
                case 3:
                    UnityEngine.Debug.LogError(finalMessage);
                break;
            }
        }
    }

    void simple_in_class_debug (System.Object message) {
        System.String className = this.GetType ().Name;
        System.String methodName = MethodBase.GetCurrentMethod ().Name;
        System.Console.WriteLine ("#" + className + "." + methodName + "()------------" + message);
    }
}