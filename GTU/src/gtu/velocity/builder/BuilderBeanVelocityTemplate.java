package ${package_path};

/**
 * @author Steve Tien
 * @version 1.0, Dec 25, 2008
 */
public class ${class_name} {

    public static class Builder {
        public static Builder newInstance() {
            return new Builder();
        }
        #foreach( $list in $fields )
        private volatile String ${list};
    	#end
        private Builder() {}
        public ${class_name} build() {
            return new ${class_name}(this);
        }
        #foreach( $list in $fields )
        public Builder ${list}(String ${list}) {
            this.${list} = ${list};
            return this;
        }
        #end
    }

    #foreach( $list in $fields )
    private final String ${list};
	#end

    private ${class_name}(Builder builder) {
        #foreach( $list in $fields )
    	this.${list} = builder.${list};
        #end
    }
    
    #foreach( $key in $fields_map.keySet())
    public String get${fields_map.get($key)}(){
    	return this.${key};
    }
    #end
    
    public String toString(){
    	return 
        #foreach( $list in $fields )
        "${list}:"+this.${list} +"\t"+
    	#end
    	;
    }
    
    public static void main(String[] args){
    	${class_name}.Builder builder = ${class_name}.Builder.newInstance();
    	#foreach( $list in $fields )
    	builder.${list}("test");
    	#end
    	${class_name} test = builder.build();
    	System.out.println(test.toString());
    }
}
