package gtu.google;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalListeners;
import com.google.common.cache.RemovalNotification;

public class LoadingCacheTest {

	public static void main(String[] args) {
		CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=200,expireAfterWrite=2m");
		CacheBuilder.from(spec);
		
		RemovalListener listener = new RemovalListener() {
			@Override
			public void onRemoval(
					RemovalNotification notify) {
				if (notify.wasEvicted()) {
                    System.out.println("log about : " + notify.getKey() +".."+notify.getValue() + " is evict!");
                }
			}
		};
		
		Executor executor = Executors.newFixedThreadPool( 5 );
        RemovalListeners.asynchronous(listener, executor);
		
		LoadingCache<String,Product> cache = CacheBuilder.newBuilder()
				.recordStats()
				.maximumSize(2000)//
				//expireAfterWrite : after this period, the object will be evicted from the cache, and replaced the next time it is requested.
				.expireAfterWrite(10, TimeUnit.MINUTES)
				//refreshAfterWrite : after this period, the object will be refreshed using the loadCache method. (with our new price)
				.refreshAfterWrite(10, TimeUnit.SECONDS)
				.removalListener(listener)//
				.build(new CacheLoader<String, Product>() {
					public Product load(String key)  {
						return createExpensive(key);
					}
				});
		
		// will return the object with given key. So in this example, the cache is not loaded all at once, but only when the object is needed.
		Product product = cache.getUnchecked("book1");

		CacheStats stats = cache.stats();
		stats.hitRate();
		stats.missRate();
		stats.loadExceptionRate();
		stats.averageLoadPenalty();
		CacheStats delta = cache.stats().minus(stats);
		delta.hitCount();
		delta.missCount();
		delta.loadSuccessCount();
		delta.loadExceptionCount();
		delta.totalLoadTime();
		System.out.println("Eviction " + stats.evictionCount());
		
		Product v = cache.getIfPresent("one");
		// returns null
		cache.put("one", new Product());
		v = cache.getIfPresent("one");
		// returns "1"
		
		//Sometimes it's necessary to simply turn off caching ,The canonical way to do this is using maximumSize(0)
	}

	//will load the product and add it to the cache, or replace it if it’s already there and needs to be refreshed.
	private static Product createExpensive(String key){
//		return dao.getProductById(key);
		return new Product();
	}
	
	static class Product{
	}

	/*
Guava的緩存抽象成了給定一個Key，返回一個Value的模式，所以你只需要實現get(K)方法就可以了，這個方法返回一個已經存在的緩存值，或者即時的計算一個值，然後緩存並返回。

返回一個Callable物件
Guava的緩存還有一個方法叫做get(K, Callable<V>)，這個方法顧名思義，就是返回值是通過Callable<V>計算出來的，其實這個方法就是把緩存值的計算邏輯抽出來了。

直接插入緩存
也可以通過cache.put(key, value)直接把緩存放進去。

移除緩存
一般情況下，我們不會讓所有的資料常駐緩存，而是讓熱點資料進入緩存，這就涉及到如何將不用的緩存移出，Guava提供了好幾種方式。

基於緩存空間的移出策略
使用CacheBuilder.maximumSize(long)限定緩存空間的大小。基本上這裡的緩存移出策略是基於LRU（最近最少使用）。

基於緩存權重的移出策略
你還可以給每一個緩存條目設定一個權重，如下代碼：

LoadingCache<Key, Graph> graphs = CacheBuilder.newBuilder()
      .maximumWeight(100000)
      .weigher(new Weigher<Key, Graph>() {
         public int weigh(Key k, Graph g) {
           return g.vertices().size();
         }
       })
      .build(
          new CacheLoader<Key, Graph>() {
            public Graph load(Key key) { // no checked exception
              return createExpensiveGraph(key);
            }
          });

基於時間的移出策略
Guava提供了兩種基於時間的移除方式，
1.	expireAfterAccess(long, TimeUnit)
2.	expireAfterWrite(long, TimeUnit)；
顧名思義，一個是最後一次訪問後的一段時間移除，還有一個是在緩存寫入的一段時間後移除。

基於引用的移出策略
1.	CacheBuilder.weakKeys()，這樣的緩存的key是弱引用的。
2.	CacheBuilder.weakValues()，緩存的value是弱引用的。
3.	CacheBuilder.softValues()，緩存的value是軟引用。
關於什麼是弱引用和軟引用（還有一個虛引用），大家都知道麼？不知道記得問老師哦。

手動移除緩存
1.	Cache.invalidateAll(keys)
2.	Cache.invalidateAll()
這幾簡單就不解釋了。

在移除的時候做點事情
Guava提供了移除緩存事件的監聽器，所以你可以在移除緩存的時候做點事情，如下代碼：

CacheLoader<Key, DatabaseConnection> loader = new CacheLoader<Key, DatabaseConnection> () {
 public DatabaseConnection load(Key key) throws Exception {
   return openConnection(key);
 }
};
RemovalListener<Key, DatabaseConnection> removalListener = new RemovalListener<Key, DatabaseConnection>() {
 public void onRemoval(RemovalNotification<Key, DatabaseConnection> removal) {
   DatabaseConnection conn = removal.getValue();
   conn.close(); // tear down properly
 }
};

return CacheBuilder.newBuilder()
 .expireAfterWrite(2, TimeUnit.MINUTES)
 .removalListener(removalListener)
 .build(loader);

上面代碼要做的就是在移除緩存的時候把資料庫連接關閉。這個特性有用吧：）

刷新緩存

可以通過LoadingCache.refresh(K)手動刷新緩存，這個需要實現reload方法，見如下代碼：

// Some keys don't need refreshing, and we want refreshes to be done asynchronously.
LoadingCache<Key, Graph> graphs = CacheBuilder.newBuilder()
      .maximumSize(1000)
      .refreshAfterWrite(1, TimeUnit.MINUTES)
      .build(
          new CacheLoader<Key, Graph>() {
            public Graph load(Key key) { // no checked exception
              return getGraphFromDatabase(key);
            }

            public ListenableFuture<Graph> reload(final Key key, Graph prevGraph) {
              if (neverNeedsRefresh(key)) {
                return Futures.immediateFuture(prevGraph);
              } else {
                // asynchronous!
                ListenableFutureTask<Graph> task = ListenableFutureTask.create(new Callable<Graph>() {
                  public Graph call() {
                    return getGraphFromDatabase(key);
                  }
                });
                executor.execute(task);
                return task;
              }
            }
          });
你也可以在構建緩存的時候指定CacheBuilder.refreshAfterWrite(long, TimeUnit)來自動刷新緩存。

其他特性

緩存統計
可以通過CacheBuilder.recordStats()來開啟緩存統計，可以通過hitRate()、verageLoadPenalty()、evictionCount()等方法獲取緩存的使用情況，預設不開啟。

asMap
cache.asMap()返回一個ConcurrentMap類型的緩存視圖。
	 */
}
