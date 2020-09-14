let a: number = 12;
let b: number = 17;

function showProduct(first: number, second: number): void {
    console.log("The product is : " + first * second);
}

// showProduct(a,b);


/**
 * Please use TypeScript/JS to answer question
 * Welcome to answer with unit testing code if you can
 *
 * After you finish coding, please push to your GitHub account and share the link with us.
 */
 
// Please write a function to reverse the following nested input value into output value
 
// Input:
let inputValue = {
    hired: {
      be: {
        to: {
          deserve: 'I'
        }
      }
    }
  };

  // Output:
  /*
let outputValue = {
    I: {
      deserve: {
        to: {
           be: 'hired'
        }
      }
    }
  };
  */


 class Stack<T> {
    _store: T[] = [];
    push(val: T) {
      this._store.push(val);
    }
    pop(): T | undefined {
      return this._store.pop();
    }
    isEmpty() : boolean {
        return this._store.length == 0;
    }
    size() : number {
        return this._store.length;
    }
  }
  

function reverseJsonObject(valObj : Object, rtnObj : Object, stack : Stack<string>): void {
    for(var k in valObj) {
        var val = (valObj as any)[k];

        if(typeof(val) == 'object') {
            stack.push(k);
            reverseJsonObject(val, rtnObj, stack);
        }else {
            stack.push(k);
            stack.push((val as string));
            let tmpObj = rtnObj;
            while(!stack.isEmpty()){
                let key : string = (stack.pop() as string);
                console.log("k -- " + key);
                if(stack.size() > 1) {
                    let child = {};
                    (tmpObj as any)[key] = child;
                    tmpObj = child;
                }else {
                    let val2 : string = (stack.pop() as string);
                    (tmpObj as any)[key] = val2;
                }
            };
        }
    }
}

let rtnObj = {};
reverseJsonObject(inputValue, rtnObj, new Stack());
console.log("Question -- " + JSON.stringify((inputValue as any)));
console.log("Answer -- " + JSON.stringify(rtnObj));
