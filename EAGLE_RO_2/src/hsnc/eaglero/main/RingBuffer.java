package hsnc.eaglero.main;

/**단방향 링형 버퍼입니다. generic class입니다.
 * @author lk.kim
 * @param <T>
 */
@SuppressWarnings("unchecked")
public class RingBuffer<T> {

    private T[] buffer;          // queue elements
    private int count = 0;          // number of elements on queue
    private int indexOut = 0;       // index of first element of queue
    private int indexIn = 0;       // index of next available slot

    // cast needed since no generic array creation in Java
    /**RingBuffer를 생성합니다.
     * @param capacity 크기
     */
    public RingBuffer(int capacity) {
        buffer = (T[]) new Object[capacity];
    }

    /**버퍼가 비었는 지 확인합니다. 
     * @return 비었으면 true
     */
    public boolean isEmpty() {
        return count == 0;
    }
    
    /**버퍼가 꽉 찼는지 확인합니다.
     * @return 꽉차지 않으면 false
     */
    public boolean isFull() {
        return count == buffer.length;
    }

    /**버퍼의 크기를 반환합니다.
     * @return int
     */
    public int size() {
        return count;
    }

    /**버퍼를 비웁니다.
     */
    public void clear() {
        for(int i = 0 ; i < count ; i++)
        	buffer[i] = null;
        
        count=0;
    }
    
    /**버퍼에 datum을 삽입합니다. 기본적으로 stack을 base로 구현된 버퍼기 때문에, FIFO 규칙을 따라서 삽입됩니다.
     * @param item
     */
    public void push(T item) {
        if (count == buffer.length) {
        	System.out.println("Ring buffer overflow");
//            throw new RuntimeException("Ring buffer overflow");
        }
        buffer[indexIn] = item;
        indexIn = (indexIn + 1) % buffer.length;     // wrap-around
        //최대치면 그대로 최대치로 두고, 아니라면 count++
        if(count++ == buffer.length)
        	count = buffer.length;
    }

    /**버퍼에서 datum을 꺼냅니다. 기본적으로 stack을 base로 구현된 버퍼기 때문에, FIFO규칙을 따라서 꺼냅니다.
     * @return
     */
    public T pop() {
        if (isEmpty()) {
        	System.out.println("Ring buffer pop underflow");
//            throw new RuntimeException("Ring buffer underflow");
        }
        
        T item = buffer[indexOut];
        buffer[indexOut] = null;                  // to help with garbage collection
        
        indexOut = (indexOut + 1) % buffer.length; // wrap-around
        
        //최소치면 그대로 최소치로 두고, 아니라면 count--
        if(count-- == 0)
        	count = 0;
  
        return item;
    }
    
    /** 버퍼에서 가장 최근에 pop한 datum의 바로 다음 datum을 복사합니다.
     * *주의* 이 함수는 값을 복사합니다.  
     * @return 값
     */
    public T next() {
//        if (isEmpty()) {
//        	System.out.println("Ring buffer next underflow");
////            throw new RuntimeException("Ring buffer underflow");
//        }
        
        return isEmpty() ? null : buffer[indexOut];
    }
}