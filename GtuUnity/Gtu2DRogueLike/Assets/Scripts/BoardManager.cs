using UnityEngine;
using System;
using System.Collections.Generic;
using Random = UnityEngine.Random;
using System.Collections.Specialized;
using static Log;

public class BoardManager : MonoBehaviour
{
    [Serializable]
    public class Count
    {
        public int minimum;
        public int maximum;

        public Count (int min, int max)
        {
            minimum = min;
            maximum = max;
        }
    }

    public int columns = 8;
    public int rows = 8;

    public Count wallCount = new Count(5, 9);
    public Count foodCount = new Count(1, 5);
    public GameObject exit;
    public GameObject[] floorTiles;
    public GameObject[] wallTiles;
    public GameObject[] foodTiles;
    public GameObject[] enemyTiles;
    public GameObject[] outerWallTiles;

    private Transform boardHolder;
    private List<Vector3> gridPositions = new List<Vector3>();

    void InitialiseList()
    {
        Log.debug("#InitialiseList --- " + "Start");
        gridPositions.Clear();
        for(int x = 1; x < columns - 1; x ++)
        {
            for(int y = 1; y < rows - 1; y ++)
            {
                gridPositions.Add(new Vector3(x, y, 0f));
            }
        }
    }

    void BoardSetup()
    {
        Log.debug("#BoardSetup --- " + "Start");
        boardHolder = new GameObject("Board").transform;
        for (int x = -1; x < columns + 1; x++)
        {
            for (int y = -1; y < rows + 1; y++)
            {
                GameObject toInstantiate = floorTiles[Random.Range(0, floorTiles.Length)];
                if(x == -1 || x == columns || y == -1 || y == rows)
                {
                    toInstantiate = outerWallTiles[Random.Range(0, outerWallTiles.Length)];
                    GameObject instance = Instantiate(toInstantiate, new Vector3(x, y, 0f), Quaternion.identity) as GameObject;
                    instance.transform.SetParent(boardHolder);
                }
            }
        }
    }

    Vector3 RandomPosition()
    {
        Log.debug("#RandomPosition --- " + "Start");
        int randomIndex = Random.Range(0, gridPositions.Count);
        Vector3 randomPosition = gridPositions[randomIndex];
        gridPositions.RemoveAt(randomIndex);
        return randomPosition;
    }

    void LayoutObjectRandom(GameObject[] tileArray, int minimum, int maximum)
    {
        Log.debug("#LayoutObjectRandom --- " + "Start");
        int objectCount = Random.Range(minimum, maximum + 1);
        for(int i = 0; i < objectCount; i ++)
        {
            Vector3 randomPosition = RandomPosition();
            GameObject tileChoice = tileArray[Random.Range(0, tileArray.Length)];
            Instantiate(tileChoice, randomPosition, Quaternion.identity);
        }
    }

    public void SetupScene(int level)
    {
        Log.debug("#SetupScene --- " + "Start");
        BoardSetup();
        InitialiseList();
        LayoutObjectRandom(wallTiles, wallCount.minimum, wallCount.maximum);
        LayoutObjectRandom(foodTiles, foodCount.minimum, foodCount.maximum);
        int enemyCount = (int)Mathf.Log(level, 2f);
        LayoutObjectRandom(enemyTiles, enemyCount, enemyCount);
        Instantiate(exit, new Vector3(columns - 1, rows - 1, 0F), Quaternion.identity);
    }

    void Start()
    {
        Log.debug("#Start --- " + "Start");
        SetupScene(1);
    }

    void Update()
    {
        // Log.debug("#Update --- " + "Start");
    }

    /*
    public override void OnInspectorGUI()
    {
        GameScript gameScript = (GameScript)target;
        if (GUILayout.Button("Choose", GUILayout.Width(70), GUILayout.Height(20)))
        {
            string filepath = EditorUtility.OpenFilePanelWithFilters("Choose", "", filters);
            EditorUtility.DisplayProgressBar("Processing..", "Shows a progress", 0.2f);
            gameScript.ProcessFile(); //It takes 8-10 sec
            EditorUtility.ClearProgressBar();
            EditorUtility.DisplayDialog("Message", "Success.", "Ok");
            GUIUtility.ExitGUI();
        }
    }
    */
}