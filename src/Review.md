# 项目

### 1.项目架构

![行情架构](/Users/zhangyi/Desktop/Java复习图/行情架构.png)



![交易中台架构](/Users/zhangyi/Desktop/Java复习图/交易中台架构.png)



### 2.DDD模型

在MVC框架之下，基本上就是事务脚本式编程，重构基本也是大方法变小方法+公共方法

随着业务需求越来越多，代码自然伴随增长，就算重构常相伴，后期再去维护时也是力不从心，要么小方法太多，要么方法太大，老人也只能匍匐前行，新人是看得懂语法却不知道语义，这也是程序员常面对的挑战，不是在编写代码，而是在摸索业务领域知识

**事务管理式编程：**

1. Session管理
2. 参数校验
3. 外部数据补全
4. 调用外部服务
5. 领域计算
6. 领域对象操作
7. 数据持久化
8. 返回

**DDD分层：**

![DDD分层](/Users/zhangyi/Desktop/Java复习图/DDD分层.png)

- **Interface层**

  对于这一层的作用就是接受外部请求，主要是HTTP和RPC

  对于Interface落地时指导方针：

  1.统一返回值，interface是对外，这样可以统一风格，降低外部认知成本2.全局异常拦截，通过aop拦截，对外形成良好提示，也防止内部异常外溢，减少异常栈序列化开销3.日志，打印调用日志，用于统计或问题定位

- **Application层**

  应用层主要作用就是编排业务,只负责业务流程串联，不负责业务逻辑

  application层其实是有固定套路的，在之前的文章有过阐述，大致流程：

  ```java
  application service method(Command command) {    
  		//参数检验    	
  		check(command);
      Aggregate aggregate = repository.findAggregate(command);
      //复杂的需要domain service    aggregate.operate(command);
      repository.saveOrUpdate(aggregate);
      publish(event);
      return DTOAssembler.to(aggregate);
  }
  ```

  **业务流程VS业务规则：**

  对于这两者怎么区分，也就是application service 与 domain service 的区分，最简单的方式：业务规则是有if/else的，业务流程没有。

  application service是很薄的一层，要把domain做厚

  对于薄与厚不再于代码的多与少，application层不是厚，而是编排多而已，逻辑很简单，一般厚的domain大多都是有比较复杂的业务逻辑，比如大量的分支条件。一个例子就是游戏里的伤害计算逻辑。另一种厚一点的就是Entity有比较复杂的状态机，比如订单

- **Domain层**

  **domain层是业务规则的集合，application service编排业务，domain service编排领域；**

  domain体现在业务语义显现化，不仅仅是一堆代码，代码即文档、代码即业务；要达到高内聚就得充分发挥domain层的优势，domain层不单单是domain service，还有entity、vo、aggregate

  **domain层内容：**

  - Aggregate
  - Service
  - Repository
  - Exception

- **Infrastructure层**

  Infrastructure层是基础实施层，为其他层提供通用的技术能力：业务平台，编程框架，持久化机制，消息机制，第三方库的封装，通用算法，等等

  **结构：**

  - dto
  - mapper
  - po
  - repository

### 3.业务内容

1. **用户账户**

   Domain层里的聚合根(aggregate root)里有账号、资金账号、累计信息（合计金额、笔数）、交易记录信息、客户流水。

   **登录和权限验证：**

   - RSA算法加密

2. **业务**

3. **基金业务**

4. **股票业务**

5. **IPO打新业务**

6. **请求业务**

### 4.如何保证一致性

分布式一致性：CAP

consul：raft算法

zookeeper：zab

### 5.出问题怎么排查

日志

top、jstack、jmap

### 秒杀系统的设计

![秒杀系统架构](/Users/zhangyi/Desktop/Java复习图/秒杀系统架构.jpeg)

#### 1.存在的问题

1. **高并发**

   秒杀的特点就是**时间极短**、 **瞬间用户量大**。

   **Redis**我感觉3-4W的QPS还是能顶得住的，但是再高了就没办法了，那这个数据随便搞个热销商品的秒杀可能都不止了。

   大量的请求进来，我们需要考虑的点就很多了，**缓存雪崩**，**缓存击穿**，**缓存穿透**

2. **超卖**

3. **恶意请求**

   搞个几十台机器搞点脚本，我也模拟出来十几万个人左右的请求

4. **链接暴露**

   在开发者模式下，点击一下**查看你的请求地址**啊

5. **数据库**

   每秒上万甚至十几万的**QPS**（每秒请求数）直接打到**数据库**，基本上都要把库打挂掉，而且你服务不单单是做秒杀的还涉及其他的业务，你没做**降级、限流、熔断**啥的，别的一起挂，小公司的话可能**全站崩溃404**。

#### 2.前端

- **资源静态化**

  秒杀一般都是特定的商品还有页面模板，现在一般都是前后端分离的，页面一般都是不会经过后端的，但是前端也要自己的服务器啊，那就把能提前放入**cdn服务器**的东西都放进去，反正把所有能提升效率的步骤都做一下，减少真正秒杀时候服务器的压力。

- **URL动态化**

  把**URL动态化**，就连写代码的人都不知道，你就通过MD5之类的摘要算法加密随机的字符串去做url，然后通过前端代码获取url后台校验才能通过。

- **限流**

  **前端限流：**这个很简单，一般秒杀不会让你一直点的，一般都是点击一下或者两下然后几秒之后才可以继续点击，这也是保护服务器的一种手段。

  **后端限流：**秒杀的时候肯定是涉及到后续的订单生成和支付等操作，但是都只是成功的幸运儿才会走到那一步，那一旦100个产品卖光了，return了一个false，前端直接秒杀结束，然后你后端也关闭后续无效请求的介入了。

- **物理控制**

  按钮**置灰**的，只有时间到了，才能点击。

#### 3.Nginx

**负载均衡**，一台服务几百，那就多搞点，在秒杀的时候多租点**流量机**。

**恶意请求拦截**也需要用到它，一般单个用户请求次数太夸张，不像人为的请求在网关那一层就得拦截掉了，不然请求多了他抢不抢得到是一回事，服务器压力上去了，可能占用网络带宽或者把**服务器打崩、缓存击穿**等等。

#### 4.风控

在请求到达后端之前，风控可以根据账号行为分析出这个账号机器人的概率大不大，每个用户的行为都是会送到大数据团队进行分析处理，打上对应标签的。

#### 5.后端

- **服务单一职责**

  设计个能抗住高并发的系统，得**单一职责**。给秒杀开个服务，只把秒杀的代码业务逻辑放一起。

  单一职责的好处就是就算秒杀没抗住，秒杀库崩了，服务挂了，也不会影响到其他的服务。（高可用）

- **Redis集群**

  之前不是说单机的**Redis**顶不住嘛，那简单多找几个兄弟啊，秒杀本来就是读多写少，那你们是不是瞬间想起来我之前跟你们提到过的，**Redis集群**，**主从同步**、**读写分离**，我们还搞点**哨兵**，开启**持久化**直接无敌高可用！

- **库存预热**

  **秒杀的本质，就是对库存的抢夺**，每个秒杀的用户来你都去数据库查询库存校验库存，然后扣减库存。开始秒杀前你通过定时任务或者运维**提前把商品的库存加载到Redis中**去，让整个流程都在Redis里面去做，然后等秒杀介绍了，再异步的去修改库存就好了。

- **事务(解决超卖)**

  Redis本身是支持事务的，而且他有很多原子命令的，大家也可以用LUA，还可以用他的管道，乐观锁他也知支持。

- **限流、降级、熔断、隔离**

  万一你真的顶不住了，**限流**，顶不住就挡一部分出去但是不能说不行，**降级**，降级了还是被打挂了，**熔断**，至少不要影响别的系统，**隔离**，你本身就独立的，但是你会调用其他的系统嘛，你快不行了你别拖累兄弟们啊。

- **消息队列（削峰填谷）**

  把它放消息队列，然后一点点消费去改库存就好了嘛，不过单个商品其实一次修改就够了，我这里说的是**某个点多个商品**一起秒杀的场景，像极了双十一零点。

#### 6.数据库

单独给秒杀建立一个数据库，为秒杀服务，表的设计也是竟可能的简单点，现在的互联网架构部署都是**分库**的。

至于表就看大家怎么设计了，该设置索引的地方还是要设置索引的，建完后记得用**explain**看看**SQL**的执行计划。

#### 7.分布式事务

几个请求丢了就丢了，要保证时效和服务的可用可靠。

所以**TCC**和**最终一致性**其实不是很适合，TCC开发成本很大，所有接口都要写三次，因为涉及TCC的三个阶段。

最终一致性基本上都是靠轮训的操作去保证一个操作一定成功，那时效性就大打折扣了。

大家觉得不那么可靠的**两段式（2PC）**和**三段式（3PC）**就派上用场了，他们不一定能保证数据最终一致，但是效率上还算ok。



### 红包系统的设计



### 正向代理、反向代理

正向代理类似一个跳板机，代理访问外部资源

比如我们国内访问谷歌，直接访问访问不到，我们可以通过一个正向代理服务器，请求发到代理服，代理服务器能够访问谷歌，这样由代理去谷歌取到返回数据，再返回给我们，这样我们就能访问谷歌了

（1）访问原来无法访问的资源，如google

​    （2） 可以做缓存，加速访问资源

　　（3）对客户端访问授权，上网进行认证



反向代理（Reverse Proxy）实际运行方式是指以代理服务器来接受internet上的连接请求，然后将请求转发给内部网络上的服务器，并将从服务器上得到的结果返回给internet上请求连接的客户端，此时代理服务器对外就表现为一个服务器

（1）保证内网的安全，阻止web攻击，大型网站，通常将反向代理作为公网访问地址，Web服务器是内网

（2）负载均衡，通过反向代理服务器来优化网站的负载

**正向代理即是客户端代理, 代理客户端, 服务端不知道实际发起请求的客户端.**


**反向代理即是服务端代理, 代理服务端, 客户端不知道实际提供服务的服务端**







# **Java基础**

### 1.基本类型

bit是最小单位，代表2进制的一位，1byte = 8bit。

Java8中基本类型：**boolean**,  

**char**(16 bits), 

**byte**(8 bits), 

**short**(16 bits), 

**int**(32 bits), 

**long**(64 bits),

 **float**(32 bits), 

**double**(64 bits). void

### 2.重载和重写

**重载**就是同样的一个方法能够根据输入数据的不同，做不同的处理。

构造器Constructor可以重载overload，不能重写overwrite。

重载发生在同一个类中，方法名必须相同，其他（参数类型、个数，返回值等）都可以不同。

**重写**就是当子类继承自负累相同的方法，输入数据一样，但要做出有别于父类的响应时，覆盖父类的方法。

返回值类型、方法名、参数列表必须相同，抛出的异常范围小于等于父类，访问修饰符范围大于等于父类。

如果父类方法访问修饰符为 `private/final/static` 则子类就不能重写该方法，但是被 static 修饰的方法能够被再次声明。构造方法无法被重写。

### 3.面向对象

**封装**：把一个对象的属性私有化，封装起来，同时提供一些可以被外界访问的属性的方法。如果一些属性不想被访问，private。

**继承**：使用已存在的类的定义作为基础建立新的类。新类可以增加新数据或功能，也可以用父类的，但不能选择继承父类。

子类拥有父类的**<u>所有</u>**属性和方法（包括私有的），但无法访问私有的。

**多态**：程序中定义的引用变量所指向的具体类型和通过该引用变量发出的方法调用在编程时并不确定，而是在程序运行期间才确定。即一个引用变量倒底会指向哪个类的实例对象以及调用的到底是哪个类的方法，只有在运行时才知道。

Java中两种方式实现多态：1.**继承**：多个子类对同一个方法的重写；2.**接口**：实现接口并覆盖接口中同一方法。

多态性是指允许不同子类型的对象对同一消息作出不同的响应。

### 4.String、StringBuffer、StringBuilder区别

String线程安全，用final修饰字符数组保存字符串。

private final char value[],   JDK9之后用private final byte[]. 

StringBuffer和StringBuilder都继承自AbstractStringBuilder，里面也用char[] value，但是没有final

StringBuffer线程安全，因为其对方法或者调用的方法加了同步锁。StringBuilder没加，不安全。

String类型进行改变的时候，生成新的String对象，指针指向新的String对象。StringBuilder一般只提升10%-15%左右的性能提升，但线程不安全。

### 5.自动拆装箱

- **装箱**：将基本类型用它们对应的引用类型包装起来；Integer i = ``10``;
- **拆箱**：将包装类型转换为基本数据类型；int` `n = i; 

### 6.Java中无参构造方法的作用

执行子类的构造方法之前，如果没用super()来调用父类特定的构造方法，则会调用父类中的无参构造方法。故如果父类只定义有参构造方法而子类没用super()调用父类特定构造方法会出错。

### 7.接口和抽象类

- 接口：
  1. 本身方法默认为public，所有方法在接口中不能有实现。(java8开始可以有默认方法和定义静态方法)
  2. 接口内部方法默认public
  3. 接口中变量都是static，final的，不能有其他变量
  4. 一个类可以实现多个接口，且接口本身可以extends扩展多个接口
  5. 设计层面：接口时对行为的抽象，是一种行为规范
- 抽象类：
  1. 本身是public，但是可以有非抽象的方法
  2. 除了static、final变量，可能有其他的变量
  3. 抽象类只能单继承
  4. 抽象类中方法可以有public、protected、default，但其就是为了被重写，所以不能有private
  5. 设计层面：抽象是对类的抽象，是一种模版设计

JDK 8 的时候接口可以有默认方法和静态方法功能。

JDK 9 在接口中引入了私有方法和私有静态方法。

JDK8: 如果同时实现两个接口，接口中定义了一样的默认方法，则必须重写，不然会报错

### 8.==与equals(重要)

- ==: 判断两个对象地址是不是想等。即判断两个对象是不是同一个对象。基本数据类型==比较的是值，引用数据类型==比较的是内存地址。
- equals()：作用也是判断两个对象是否想的，但一般有两种使用情况
  1. 类没有覆盖equals()方法。则通过equals()等价于通过==比较
  2. 覆盖了equals()方法，那就通过equals()方法判断。若equals()返回true，认为俩对象相等

当创建 String 类型的对象时，虚拟机会在常量池中查找有没有已经存在的值和要创建的值相同的对象，如果有就把它赋给当前引用。如果没有就在常量池中重新创建一个 String 对象。

### 9.hashCode与equals(重要)

如果不会在HashSet, Hashtable, HashMap等等这些本质是散列表的数据结构中，用到该类。例如，不会创建该类的HashSet集合。在这种情况下，该类的“hashCode() 和 equals() ”没有半毛钱关系的！

- hashCode()：获取哈希码，是int整数，通常用来将对象的内存地址换为整数后返回。
- 为什么重写equals时必须重写hashCode()：若两个对象相等，hashcode一定相等。两个对象分别调用equals返回true。但两个对象有相同hashcode值，它们不一定相等。因此equals方法被覆盖，hashCode也得重写。hashCode()默认从堆上产生独特值，不重写，该class两个对象无论如何不相等。

### 10.解决哈希碰撞的方法

1. 链接法：数组加链表

2. 除法散列法：h(k) = k mod m; 其中k时槽中数值，m是数组大小。

3. 乘法散列法

4. 开放寻址

   1～n-1，全排列n!，尽量分散，用n!种方式，一致散列。但是不能完全一致散列，一般用近似的。：线性探查、二次探查、双重探查。这些都不能实现一致散列，最多产生的探查序列最多m^2个。

   - 线性探查：先找到第一个地址，若被占用，线性往下找。

   - 二次探查：优化线性探查，每次移动一个偏移量。

   - 双重散列：利用两个散列函数

     h(k,i) = (h1(k) + i*h2(k)) % 10,其中h1(k)和h2(k)是两个散列函数。如h1(k) = k%3, h2(k)=k%7.这样使得初始位置相同而后续探查序列不同。h2(k)要和m互质

### 11.一致性哈希

**将整个哈希值空间组织称一个虚拟的hash环，对2^32取模，将目标分散的放在圆环上。空间为2^32-1,各服务器使用Hash进行哈希运算，分配在不同地址。具体可以选择服务器的IP或主机名作为关键字进行哈希**。

**对象进来也进行哈希，对应到环上然后进行顺时针分配到服务器。**

**一致性哈希为什么是2^ 32次方：因为一般用服务器的IP地址做一致性哈希，服务器IP地址是2^32**

服务节点太少，容易因为节点分布不均匀面造成`数据倾斜（被缓存的对象大部分缓存在某一台服务器上）问题`，解决方法是引入虚拟节点，即对每一个节点计算多个哈希，每个计算结果都放一个该服务节点。

![虚拟节点一致性hash](/Users/zhangyi/Desktop/Java复习图/虚拟节点一致性hash.png)

### 1.Java异常

![img](https://guide-blog-images.oss-cn-shenzhen.aliyuncs.com/2020-12/Java%E5%BC%82%E5%B8%B8%E7%B1%BB%E5%B1%82%E6%AC%A1%E7%BB%93%E6%9E%84%E5%9B%BE.png)

**RuntimeException、IOException** 

当 try 语句和 finally 语句中都有 return 语句时，在方法返回之前，finally 语句的内容将被执行，并且 finally 语句的返回值将会覆盖原始的返回值。

### 13.元注解（共4个）

1. @Target：用于什么地方
2. @Retention：在什么级别
3. @Document：注解将被包含在javadoc
4. @Inherited：子类可继承父类

### 14.反射

对任一个类，都能知道累的所有属性和方法。

动态获取信息以及动态调用方法的功能

# Java集合

![Java集合整体图](/Users/zhangyi/Desktop/Java复习图/Java集合整体图.jpeg)

### 1.Collection

主要两个分支：**List**和**Set**。

它是一个接口，是高度抽象出来的集合，包含集合的基本操作：添加、删除、清空、遍历、是否为空、取大小等。

1. **List**：继承于Collection接口，是有序的队列。List中每个元素都有索引，第一个元素的为0，后面的依次+1。List中可以有重复元素。

2. **Set：Set**是一个继承于Collection的接口，即Set也是集合中的一种。Set是没有重复元素的集合。

3. **AbstractCollection**：是一个抽象类，实现了Collection中除了iterator()和size()外的函数。

   AbstractCollection的主要作用：它实现了Collection接口中的大部分函数。从而方便其它类实现Collection，比如ArrayList、LinkedList等，它们这些类想要实现Collection接口，通过继承AbstractCollection就已经实现了大部分的接口了。

4. **Iterator：Iterator**是一个接口，它是集合的迭代器。集合可以通过Iterator去遍历集合中的元素。Iterator提供的API接口，包括：是否存在下一个元素、获取下一个元素、删除当前元素。

   注意：Iterator遍历Collection时，是fail-fast机制的。即，当某一个线程A通过iterator去遍历某集合的过程中，若该集合的内容被其他线程所改变了；那么线程A访问集合时，就会抛出ConcurrentModificationException异常，产生fail-fast事件。

5. **ListIterator**： ListIterator是一个继承于Iterator的接口，它是队列迭代器。专门用于遍历List，能提供向前/向后遍历。相比于Iterator，它新增了添加、是否存在上一个元素、获取上一个元素等等API接口。

### 2.ArrayList

两个重要的对象：elementData和size

elementData是Object[]类型数组，保存了添加到ArrayList中的元素。实际上，elementData是个动态数组，可以通过构造函数ArrayList(int initialCapacity)来初始化它的 **initialCapacity** 容量。否则默认是**10**。elementData数组大小会根据ArrayList的容量动态增长。size是动态数组的大小。 

ArrayList和Vector：都是List的实现类，但是Vector比较古老。ArrayList线程不安全，Vector线程安全，因为Vector加了synchronized锁。

遍历的三种方式：

1. 迭代器Iterator：`while(list.iterator.hasNext())`

2. foreach循环

3. RandomAccess接口：快速随机访问

   是一个标识，接口中什么也没定义，只是在binarySearch()方法中做判断用。如果是，调用`indexedBinarySearch()`方法，如果不是，那么调用`iteratorBinarySearch()`方法

##### **ArrayList扩容机制**：

底层是动态数组，通过ensureCapacity操作增加容量。

**以无参数构造方法创建 `ArrayList` 时，实际上初始化赋值的是一个空数组。当真正对数组进行添加元素操作时，才真正分配容量。即向数组中添加第一个元素时，数组容量扩为 10。**

- **grow()**方法，每次扩容后容量变为原来的1.5倍左右。偶数就是1.5倍，奇数是1.5倍左右。

  **int newCapacity = oldCapacity + (oldCapacity >> 1)**

- **System.arraycopy()方法**

  ```java
  /**    
  *   复制数组    
  * @param src 源数组    
  * @param srcPos 源数组中的起始位置    
  * @param dest 目标数组    
  * @param destPos 目标数组中的起始位置    
  * @param length 要复制的数组元素的数量    
  */    
  public static native void arraycopy(Object src,  int  srcPos,                                        Object dest, int destPos,                                        int length);
  ```

- **Arrays.copyOf()** **用于给数组扩容**

  ```java
  public static int[] copyOf(int[] original, int newLength) {        
    // 申请一个新的数组        
    int[] copy = new int[newLength];    
    // 调用System.arraycopy,将源数组中的数据进行拷贝,并返回新的数组        
    System.arraycopy(original, 0, copy, 0,                         Math.min(original.length, newLength));
    return copy;    }
  ```

- **ensureCapacity(int minCapacity)在add大量元素前用，以减少重新分配次数**

##### **fail-fast机制：**

是一种错误检测机制，当多线程同时操作ArrayList的时候，其中一个发现list被改动会报错。这种情况用java.util.concurrent包下的CopyOnWriteArrayList。

fail-fast利用modCount来实现的，原理：

无论add(),remove()还是clear(),只要修改集合中元素个数时，都改变modCount的值。然后每次next和remove等操作时，会执行checkForComodificaion()来判断modCount和expectedModCount是否相等。不等就报错。



### 3.HashMap

##### **和HashTable区别**：

1. **线程是否安全**：

   HashMap线程非安全，HashTable线程安全，因为其内部基本都用synchronized修饰，但是效率低，一般要保证线程安全就用ConcurrentHashMap。

2. **对Null key和Null value的支持**：

   HashMap可以存储null的key和value，但null作为键只能有一个，作为值可以有多个；HashTable不允许有null键和null值，否则报错NullPointerException；

3. **初始容量大小和每次扩容大小**：

   - 创建时如果不指定容量初始值，HashTable默认初始值大小为11，之后每次扩充，容量变为2n+1；HashMap默认初始化大小为16，之后每次扩充为原来的2倍。
   - 如果设置容量初始值，那么HashTable会直接用指定大小，HashMap扩充为2的幂次方大小

4. **底层数据结构**：

   HashMap在JDK1.8之前为数组加链表，之后为数组加链表，但链表长度超过8时，链表会转为红黑树。(但是转红黑树前判断数组长度是否大于64，不大于则先数组扩容)

##### **和HashSet区别**：

HashSet底层就是基于HashMap的

| `HashMap`                              | `HashSet`                                                    |
| -------------------------------------- | ------------------------------------------------------------ |
| 实现了 `Map` 接口                      | 实现 `Set` 接口                                              |
| 存储键值对                             | 仅存储对象                                                   |
| 调用 `put()`向 map 中添加元素          | 调用 `add()`方法向 `Set` 中添加元素                          |
| `HashMap` 使用键（Key）计算 `hashcode` | `HashSet` 使用成员对象来计算 `hashcode` 值，对于两个对象来说 `hashcode` 可能相同，所以` equals()`方法用来判断对象的相等性 |

HashSet检查重复：HashSet会先计算对象的hashcode来判断对象加入的位置，然后也加入其他对象的hashcode进行比较。如果发现有相同hashcode值的对象，调用equals()方法比较，如果相同，不让插入。



##### **HashMap底层结构**：

底层时链表加数组实现的，也就是链表散列。

**负载因子为loadCapacity=0.75，当存储数量超过容量*负载因子时，进行扩容。初始值为16，故达到12后扩容，每次扩容时容量翻倍。**

HashMap 通过 key 的 hashCode 经过扰动函数处理过后得到 hash 值，然后通过 (n - 1) & hash 判断当前元素存放的位置（这里的 n 指的是数组的长度），如果当前位置存在元素的话，就判断该元素与要存入的元素的 hash 值以及 key 是否相同，如果相同的话，直接覆盖，不相同就通过拉链法解决冲突。

JDK1.8之后，当链表长度大于8之后，链表转成红黑树。为什么阈值时8，因为二叉树查找复杂度为log(n),当为8时，红黑树的复杂度为log(8)=3；链表的查询复杂度为n/2，所以为8时链表查询复杂度为4，故阈值为8.



##### **红黑树详解**：

1. 红黑树是平衡二叉树，维持树的深度保持在log(n)左右。红黑树的特征：

   1. 每个节点不是红色就是黑色
   2. 根节点为黑色
   3. 每个叶子节点都是黑色
   4. 每个红色节点的子节点都是黑色
   5. 任意节点，到其任意叶节点的所有路径都包含相同的黑色节点

   ![红黑树左旋](/Users/zhangyi/Desktop/Java复习图/红黑树左旋.jpeg)

   ![红黑树右旋](/Users/zhangyi/Desktop/Java复习图/红黑树右旋.jpeg)

##### **HashMap的长度为什么是2的幂次方**

这个数组下标的计算方法是“ `(n - 1) & hash`”

**“取余(%)操作中如果除数是2的幂次则等价于与其除数减一的与(&)操作（也就是说 hash % length == hash & (length-1)的前提是 length 是2的 n 次方；）。”** 

**2的幂次方-1之后二进制所有位都是1.**

**并且采用二进制位操作 &，相对于%能够提高运算效率，这就解释了 HashMap 的长度为什么是2的幂次方。**



##### **HashMap多线程死锁**：

JDK1.7的时候HashMap扩容的时候采用的是头插法，多线程操作下会产生死循环。JDK1.8之后改为尾插法，但多线程还是使用ConcurrentHashMap



##### **ConcurrentHashMap**

- 底层数据结构：JDK1.7的时候采用分段数组+链表来实现，JDK1.8采用根HashMap1.8一样的数组+链表/红黑树的结构。
- 实现线程安全的方式：1.7的时候，对整个桶数组进行了分段(Segment)，每一把锁只锁容器其中一部分数据。到了1.8的时候摒弃了Segment，直接用Node数组+链表+红黑树的结构来实现，**并使用synchronized和CAS**来操作

底层实现：

1. JDK1.7使用Segment数组和HashEntry，Segment实现了ReentrantLock，所以Segment是可重入锁。HashEntry用于存储键值对。
2. put操作：
   - 如果相应位置的Node还未初始化，使用CAS进行插入相应的数据
   - 如果相应的Node不为空，且当前节点不处于移动状态，则对该节点加synchronized锁；如果hash不小于0，遍历链表更新节点或插入新节点。
   - 如果节点是TreeBin类型，说明是红黑树结构，putTreeVal方法插入节点
   - 如果`binCount`不为0，说明`put`操作对数据产生了影响，如果当前链表的个数达到8个，则通过`treeifyBin`方法转化为红黑树，如果`oldVal`不为空，说明是一次更新操作，没有对元素个数产生影响，则直接返回旧值；
3. size实现：1.8中用一个volatile类型变量baseCount记录元素个数，插入或删除数据，用addCount()方法更新baseCount()；

### 4.集合总结

1. List
   - ArrayList：Object[]数组
   - Vector：Object[]数组
   - LinkedList：双向链表
2. Set
   - HashSet：无序的，底层为HashMap
   - LinkedHashSet：HashSet的子类，内部通过LinkedHashMap实现
   - TreeSet：有序的，底层红黑树
3. Map
   - HashMap
   - LinkedHashMap，继承自HashMap
   - HashTable
   - TreeMap：底层红黑树



# Java多线程

### 1.Java线程状态

![Java线程状态](/Users/zhangyi/Desktop/Java复习图/Java线程状态.jpeg)

线程共包含5种状态：

1. 新建状态(New)：线程对象被创建后，就进入了新建状态。例如，Thread thread = new Thread()。
2. 就绪状态(Runnable)：也被称为“可执行状态”。线程对象被创建后，其他线程调用了该对象的start()方法，从而来启动线程。如thread.start()。就需台的线程随时可能被CPU调度执行。
3. 运行状态(Running)：线程获取CPU权限进行执行。需要注意的是，线程只能从就绪状态进入到运行状态。
4. 阻塞状态(Blocked)：阻塞状态是线程因为某种原因放弃CPU使用权，暂时停止运行。直到线程进入就绪状态，才有机会转到运行状态。阻塞的情况分三种:
   - 等待阻塞：通过调用线程的wait()方法，让线程等待某工作的完成。
   - 同步阻塞：线程在获取synchronized同步锁失败，会进入同步阻塞
   - 其他阻塞：通过调用线程的sleep()或join()或发出I/O请求时，线程会进入到阻塞状态。当sleep()状态超时、join()等待线程终止或超时、或者I/O处理完毕时，线程重新进入就绪状态
5. 死亡状态(Dead)：线程执行完或者因为异常退出run()方法，该线程结束生命周期。

### 2.死锁

**死锁**产生的4个必要条件：

1. 互斥：资源任意时刻只能由一个线程占用
2. 占有并等待：线程占有资源不释放的同时，等待资源释放
3. 非抢占：线程获得的资源在未使用完之前不会被强行抢占，只有使用完才释放。
4. 循环等等：若干线程之间形成头尾相接的循环等等关系

**如何避免死锁**：破坏四个必要条件中任意一个就行

### 3.sleep()和wait()和join()的异同

1. **最主要区别**：sleep()方法没有释放锁，wait()释放了锁，join() 的作用：让“主线程”等待“子线程”结束之后才能继续运行。
2. wait()通常被用于线程间交互/通信，sleep()用于暂停执行
3. wait()方法被调用后，线程不会自动苏醒，需要别的线程调用同一个对象上的notify()或者notifyAll()。而sleep()执行完之后，线程会自动苏醒。或者用wait(long timeout)，超时之后线程自动苏醒。

### 4.为什么调用start()会执行run()

因为new一个Thread之后线程进入了新建状态。调用start()方法，会启动一个线程并使线程进入就绪状态，分配到时间片就开始运行了。start()会执行线程的相应准备工作，然后自动执行run()方法的内容，是真正的多线程工作。但是直接执行run()方法，会将其当成main线程下的普通方法执行。

**总结： 调用 `start()` 方法方可启动线程并使线程进入就绪状态，直接执行 `run()` 方法的话不会以多线程的方式执行。**

### 5.Synchronized（重要）

1. 早期属于**重量级锁**，效率低下，原因是监视器锁(moniter)是依赖于底层操作系统**Mutex Lock**实现的，Java的线程是映射到操作系统的原生线程之上的。如果要挂起或唤醒一个线程，需要操作系统帮忙，但是操作系统实现线程之间的切换时需要从用户态转换到内核态，这个状态之间的转换需要相对比较长的时间，时间成本相对较高。

   Java6之后从JVM层面进行了优化，引入了偏向锁、轻量级锁、自旋锁、锁粗化、锁消除等技术。

2. synchronized三种使用方式：

   1. 修饰实例方法：作用于当前对象实例加锁，进入代码前要获取**当前对象实例**的锁

      ```java
      synchronized void method() {  
        //业务代码 
      }
      ```

   2. 修饰静态方法，也就是给类加锁，作用于类的所有对象实例，进入代码前要获取**当前class**的锁。如果一个线程 A 调用一个实例对象的非静态 `synchronized` 方法，而线程 B 需要调用这个实例对象所属类的静态 `synchronized` 方法，是允许的，不会发生互斥现象，**因为访问静态 `synchronized` 方法占用的锁是当前类的锁，而访问非静态 `synchronized` 方法占用的锁是当前实例对象锁**。

      ```java
      synchronized staic void method() {  
        //业务代码 
      }
      ```

   3. 修饰代码块：指定加锁对象/类

      `synchronized(this|object)` 表示进入同步代码库前要获得**给定对象的锁**。`synchronized(类.class)` 表示进入同步代码前要获得 **当前 class 的锁**

      synchronized(this) {  
        //业务代码 
      }

3. 双重校验锁实现单例：

   ```java
   public class Singleton{
     private volatile static Singleton instance;
     private Singleton(){};
   
     public static Singleton getInstance(){
       if (instance == null){
         synchronized (Singleton.class){
           if(instance == null){
             instance = new Singleton();
           }
         }
       }
       return instance;
     }
   }
   ```

   需要注意 `instance` 用 `volatile` 关键字修饰很有必要。

   `instance` 采用 `volatile` 关键字修饰是很有必要的， `instance = new Singleton();` 代码其实是分为三步：

   1. 为 `instance` 分配内存空间
   2. 初始化 `instance`
   3. 将 `instance` 指向分配的内存地址

   但是由于 **JVM 具有指令重排**的特性，执行顺序有可能变成 1->3->2。指令重排在单线程环境下不会出现问题，但是在多线程环境下会导致一个线程获得还没有初始化的实例。例如，线程 T1 执行了 1 和 3，此时 T2 调用 `getinstance()` 后发现 `instance` 不为空，因此返回 `instance`，但此时 `instance` 还未被初始化。

   使用 `volatile` 可以**禁止 JVM 的指令重排**，保证在多线程环境下也能正常运行。

4. **构造方法不能使用 synchronized 关键字修饰。**

   构造方法本身就属于线程安全的，不存在同步的构造方法一说。

   

5. **synchronized原理：**

   **`synchronized` 同步语句块的实现使用的是 `monitorenter` 和 `monitorexit` 指令，其中 `monitorenter` 指令指向同步代码块的开始位置，`monitorexit` 指令则指明同步代码块的结束位置。**

   在执行`monitorenter`时，会尝试获取对象的锁，如果锁的计数器为 0 则表示锁可以被获取，获取后将锁计数器设为 1 也就是加 1。

   在执行 `monitorexit` 指令后，将锁计数器设为 0，表明锁被释放。如果获取对象锁失败，那当前线程就要阻塞等待，直到锁被另外一个线程释放为止。

   

6. **synchronized和volatile区别**

   synchronized和volatile是互补的存在

   - **`volatile` 关键字**是线程同步的**轻量级实现**，所以**`volatile`性能肯定比`synchronized`关键字要好**。但是**`volatile` 关键字只能用于变量而 `synchronized` 关键字可以修饰方法以及代码块**。

   - **`volatile` 关键字能保证数据的可见性，但不能保证数据的原子性。`synchronized` 关键字两者都能保证。**
   - **`volatile`关键字主要用于解决变量在多个线程之间的可见性，而 `synchronized` 关键字解决的是多个线程之间访问资源的同步性。**

7. synchronized锁升级

   **Java对象头里Mark Word结构：**

   | 锁状态   | 是否是偏向锁                      |
   | -------- | --------------------------------- |
   | 轻量级锁 | 指向栈中锁记录的指针              |
   | 重量级锁 | 指向互斥量（重量级锁）的指针      |
   | GC标记   | 空                                |
   | 偏向锁   | 线程ID / Epoch / 对象分代年龄 / 1 |

   **锁升级的对比：**

   Java1.6为了减少获得和释放锁带来的性能消耗，引入了偏向锁和轻量级锁。一共**4种**锁状态：**无锁状态、偏向锁状态、轻量级锁、重量级锁**

   1. 偏向锁：

      - 当一个线程访问同步块并获取锁时，会在**对象头和栈帧中的锁记录**里存储**锁偏向的线程ID**，以后该线程在进入和退出同步块的时候，不需要再进行CAS操作来加锁解锁，只需要简单测试mark word里是否存着指向当前线程的偏向锁。
      - 偏向锁本身是在没有竞争时存在的，使用了一种等到竞争出现才释放锁的机制。当竞争出现时，偏向锁会等到全局安全点出现，然后暂停有锁的线程。接着检查持有偏向锁的线程是否还活着，如果不处于活动状态，将对象头设置为无锁；如果活着，拥有偏向锁的栈会执行，遍历偏向锁的记录。栈中的锁记录和对象头的mark word要么重新偏向另外线程，要么恢复无锁或标记对象不适合作为偏向锁，唤醒等等线程。

   2. 轻量级锁

      - 加锁：线程执行同步块前，JVM在栈帧中创建用于存储锁记录的空间，将对象头中的Mark Word复制到锁记录（Displaced Mark Word）。然后线程尝试用CAS将对象头中的Mark Word替换为指向锁记录的指针。如果成功，获取，否则则存在竞争，尝试自旋获取锁。

      - 解锁：

        使用原子的CAS将Displaced Mark Word换回到对象头，成功，没有竞争。失败，有竞争，膨胀成重量级锁。

      自旋会消耗CPU，为了避免无用自旋，一旦升级成重量级锁，一般回不去轻量级。

      ```c
      // CAS:在CPU层级时原子性的
      // p是加锁的线程，old是老的获取锁线程，
      // new是新来的线程
      funciton CAS(p, old, new)
      {
        if(*p != old){
          return false;
        }
        *p <- new;
        return true;
      }
      ```

   3. CAS实现原子操作有三大问题：

      1. ABA问题

         因为CAS在操作值的时候，检查值有没有变化，如果没有变化则更新。但是如果一个值原来时A，在自旋期间变成B但又变回A，进行检查时发现值没变，但实际却是变化了。其真正问题在于：二进制上依旧为A但语义已经不是A，如整数溢出、内存回收等。解决办法：加上版本号，每次更新的时候也更新版本号。

      2. 循环时间长，开销大

         如果CAS自旋长时间不成功，会给CPU带来非常大的执行开销。如果JVM支持处理器提供的pause指令，那么效率就有一定提升。

      3. 只能保证一个共享变量的原子操作

         当对多个共享变量操作的时候，CAS循环无法保证操作原子性，这个时候可以用锁。或者把多个共享变量合成一个。

### 6.原子操作的实现原理

1. 使用总线锁保证

   处理器使用一个LOCK#信号，当一个处理器在总线上输出此信号时，其他处理器请求将被阻塞，改处理器就可以独占共享内存。

2. 使用缓存锁保证

   内存区域如果被缓存在处理器的缓存行中，并在Lock操作期间被锁定，那么当它执行锁操作回写到内存时，处理器不在总线上声言LOCK#信号，而是修改内部内存地址，并允许它的缓存一致性机制来保证原子性，因为缓存一致性原则会阻止同时修改两个以上处理器缓存的内存区域，当其他处理器回写已经被锁定的缓存行数据，会使缓存行失效。

### 7.内存屏障和指令重排

- 指令重排

   **指令优化的时候，会产生指令重排**的特性，执行顺序有可能变成 1->3->2。指令重排在单线程环境下不会出现问题，但是在多线程环境下会导致一个线程获得还没有初始化的实例。例如，线程 T1 执行了 1 和 3，此时 T2 调用 `getinstance()` 后发现 `instance` 不为空，因此返回 `instance`，但此时 `instance` 还未被初始化。

  使用 `volatile` 可以**禁止 JVM 的指令重排**，保证在多线程环境下也能正常运行

  **重排序问题无时无刻不在发生，源自三种场景：**

  1. 编译器编译时的优化
  2. 处理器执行时的乱序优化
  3. 缓存同步顺序（导致可见性问题）

  ![计算机缓存架构](/Users/zhangyi/Desktop/Java复习图/计算机缓存架构.png)

- ##### MESI协议

  *在Core0修改了数据v后，让Core1在使用v前，能得到v最新的修改值*。

  1. **Core0修改v后，发送一个信号，将Core1缓存的v标记为失效，并将修改值写回内存。**
  2. **Core0可能会多次修改v，每次修改都只发送一个信号（发信号时会锁住缓存间的总线），Core1缓存的v保持着失效标记。**
  3. **Core1使用v前，发现缓存中的v已经失效了，得知v已经被修改了，于是重新从其他缓存或内存中加载v。**

  

- **内存屏障**

  通过**volatile标记，可以解决编译器层面的可见性与重排序问题**。而**内存屏障则解决了硬件层面的可见性与重排序问题**。

  **内存屏障的实现是针对乱序执行的过程来设计**。指令会乱序执行的基本原理：是指令会被放入一个**序列缓冲区**，只要指令的数据运算对象是可以获取的，指令就被允许在先进入的、旧的指令之前离开序列缓冲区，开始执行。

  - Store：将处理器缓存的数据刷新到内存中。
  - Load：将内存存储的数据拷贝到处理器的缓存中。

  | 屏障类型            | 指令示例                 | 说明                                                         |
  | :------------------ | :----------------------- | :----------------------------------------------------------- |
  | LoadLoad Barriers   | Load1;LoadLoad;Load2     | **该屏障确保Load1数据的装载先于Load2及其后所有装载指令的的操作** |
  | StoreStore Barriers | Store1;StoreStore;Store2 | **该屏障确保Store1立刻刷新数据到内存(使其对其他处理器可见)的操作先于Store2及其后所有存储指令的操作** |
  | LoadStore Barriers  | Load1;LoadStore;Store2   | **确保Load1的数据装载先于Store2及其后所有的存储指令刷新数据到内存的操作** |
  | StoreLoad Barriers  | Store1;StoreLoad;Load2   | **该屏障确保Store1立刻刷新数据到内存的操作先于Load2及其后所有装载装载指令的操作。它会使该屏障之前的所有内存访问指令(存储指令和访问指令)完成之后,才执行该屏障之后的内存访问指令** |

  **StoreLoad Barriers同时具备其他三个屏障的效果，因此也称之为`全能屏障`（mfence），是目前大多数处理器所支持的；但是相对其他屏障，该屏障的开销相对昂贵。**

  ## x86架构的内存屏障

  x86架构并没有实现全部的内存屏障。

  ### Store Barrier

  sfence指令实现了Store Barrier，相当于StoreStore Barriers。

  强制所有在sfence指令之前的store指令，都在该sfence指令执行之前被执行，发送缓存失效信号，并把store buffer中的数据刷出到CPU的L1 Cache中；所有在sfence指令之后的store指令，都在该sfence指令执行之后被执行。即，禁止对sfence指令前后store指令的重排序跨越sfence指令，使**所有Store Barrier之前发生的内存更新都是可见的**。

  ### Load Barrier

  lfence指令实现了Load Barrier，相当于LoadLoad Barriers。

  强制所有在lfence指令之后的load指令，都在该lfence指令执行之后被执行，并且一直等到load buffer被该CPU读完才能执行之后的load指令（发现缓存失效后发起的刷入）。即，禁止对lfence指令前后load指令的重排序跨越lfence指令，配合Store Barrier，使**所有Store Barrier之前发生的内存更新，对Load Barrier之后的load操作都是可见的**。

  ### Full Barrier

  mfence指令实现了Full Barrier，相当于StoreLoad Barriers。

  mfence指令综合了sfence指令与lfence指令的作用，强制所有在mfence指令之前的store/load指令，都在该mfence指令执行之前被执行；所有在mfence指令之后的store/load指令，都在该mfence指令执行之后被执行。即，禁止对mfence指令前后store/load指令的重排序跨越mfence指令，使**所有Full Barrier之前发生的操作，对所有Full Barrier之后的操作都是可见的。**

  

  ## volatile如何解决内存可见性与处理器重排序问题

  > 在编译器层面，仅将volatile作为标记使用，取消编译层面的缓存和重排序。

  VM对volatile变量的处理如下：

  - 在写volatile变量v之后，插入一个sfence。这样，sfence之前的所有store（包括写v）不会被重排序到sfence之后，sfence之后的所有store不会被重排序到sfence之前，禁用跨sfence的store重排序；且sfence之前修改的值都会被写回缓存，并标记其他CPU中的缓存失效。
  - 在读volatile变量v之前，插入一个lfence。这样，lfence之后的load（包括读v）不会被重排序到lfence之前，lfence之前的load不会被重排序到lfence之后，禁用跨lfence的load重排序；且lfence之后，会首先刷新无效缓存，从而得到最新的修改值，与sfence配合保证内存可见性。

  ## final关键字

  如果一个实例的字段被声明为final，则JVM会在初始化final变量后插入一个sfence。

  > 类的final字段在`<clinit>()`方法中初始化，其可见性由JVM的类加载过程保证。

  final字段的初始化在`<init>()`方法中完成。sfence禁用了sfence前后对store的重排序，且保证final字段初始化之前（include）的内存更新都是可见的。

  

### 8.volatile

1. 是轻量级的synchronized，在多处理器开发中保证了共享变量的**可见性**。

   原理：被volatile修饰的共享变量编译以后会带有一个**Lock前缀**的指令，其在多核处理器下会引发两件事情：

   1. 将当前处理器缓存行的内容写回到内存
   2. 这个写回内存的操作会使在其他cpu缓存了该内存地址的数据无效。

2. 于是在多处理器下，通过缓存一致性协议，当处理器发现自己缓存行对应内存被修改，会将当前缓存行设置成无效状态，当处理器修改数据时，从内存重新读区数据。

3. Java内存模型

   而在当前的 Java 内存模型下，线程可以把变量保存**本地内存**（比如机器的寄存器）中，而不是直接在主存中进行读写。这就可能造成一个线程在主存中修改了一个变量的值，而另外一个线程还继续使用它在寄存器中的变量值的拷贝，造成**数据的不一致**。

   ![Java内存模型](file:///Users/zhangyi/Desktop/Java%E5%A4%8D%E4%B9%A0%E5%9B%BE/Java%E5%86%85%E5%AD%98%E6%A8%A1%E5%9E%8B.png?lastModify=1635567041)

   要解决这个问题，就需要把变量声明为**`volatile`，这就指示 JVM，这个变量是共享且不稳定的，每次使用它都到主存中进行读取。**所以，**`volatile` 关键字 除了防止 JVM 的指令重排 ，还有一个重要的作用就是保证变量的可见性。**

   ![volatile](/Users/zhangyi/Desktop/Java复习图/volatile.png)

### 9.ThreadLocal

1. **`ThreadLocal`类主要解决的就是让每个线程绑定自己的值，可以将`ThreadLocal`类形象的比喻成存放数据的盒子，盒子中可以存储每个线程的私有数据。**

   **如果你创建了一个ThreadLocal变量，那么访问这个变量的每个线程都会有这个变量的本地副本，这也是ThreadLocal变量名的由来。他们可以用get()和set()方法来获取默认值或将其值改为当前进程锁存的副本值，从而避免线程安全问题。**

   **最终的变量是放在了当前线程的 `ThreadLocalMap` 中，并不是存在 `ThreadLocal` 上，`ThreadLocal` 可以理解为只是`ThreadLocalMap`的封装，传递了变量值。** `ThrealLocal` 类中可以通过`Thread.currentThread()`获取到当前线程对象后，直接通过`getMap(Thread t)`可以访问到该线程的`ThreadLocalMap`对象。

   **每个`Thread`中都具备一个`ThreadLocalMap`，而`ThreadLocalMap`可以存储以`ThreadLocal`为 key ，Object 对象为 value 的键值对。**

   ![ThreadLocal内部原理](/Users/zhangyi/Desktop/Java复习图/ThreadLocal内部原理.png)

2. **ThreadLocal内存泄露**

   `ThreadLocalMap` 中使用的 key 为 `ThreadLocal` 的弱引用,而 value 是强引用。所以，如果 `ThreadLocal` 没有被外部强引用的情况下，在垃圾回收的时候，key 会被清理掉，而 value 不会被清理掉。这样一来，`ThreadLocalMap` 中就会出现 key 为 null 的 Entry。假如我们不做任何措施的话，value 永远无法被 GC 回收，这个时候就可能会产生内存泄露。

   ![ThreadLocal内存泄露](/Users/zhangyi/Desktop/Java复习图/ThreadLocal内存泄露.png)

### 10.线程池

线程池提供了一种限制和管理资源（包括执行一个任务）。每个线程池还维护一些基本的统计信息，提高对资源的利用率。

线程池好处：

- 降低资源消耗
- 提高响应的速度
- 提高线程的可管理性

1. **Runnable和Callable的区别**

   Runnable不返回结果和异常，Callable可以返回结果和异常

   `Runnable.java`

   ```java
   
   @FunctionalInterface public interface Runnable {   
     /**    * 被线程执行，没有返回值也无法抛出异常    */    
     public abstract void run(); 
   }
   ```

   `Callable.java`

   ```java
   @FunctionalInterface public interface Callable<V> {    
   /**     
    * 计算结果，或在无法这样做时抛出异常。         	* @return 计算得出的结果     
     * @throws 如果无法计算结果，则抛出异常     */    
     V call() throws Exception; 
   }
   ```

2. ##### **执行execute()方法和submit()方法的区别是什么**

   1. **`execute()`方法用于提交不需要返回值的任务，所以无法判断任务是否被线程池执行成功与否；**
   2. **`submit()`方法用于提交需要返回值的任务。线程池会返回一个 `Future` 类型的对象，通过这个 `Future` 对象可以判断任务是否执行成功**，并且可以通过 `Future` 的 `get()`方法来获取返回值，`get()`方法会阻塞当前线程直到任务完成，而使用 `get（long timeout，TimeUnit unit）`方法则会阻塞当前线程一段时间后立即返回，这时候有可能任务没有执行完。

   **补充说明一点，submit()实际上也是通过调用execute()实现的，源码如下：**

   ```java
   public Future<?> submit(Runnable task) {
       if (task == null) throw new NullPointerException();
       RunnableFuture<Void> ftask = newTaskFor(task, null);
       execute(ftask);
       return ftask;
   }
   ```

3. **如何创建线程池**

   不要使用Executors去创建线程池，通过ThreadExecutor的方式创建，这样能更加明确线程池运行规则，规避资源耗尽风险。

   **Executors返回线程池对象的弊端如下**：

   - FixedThreadPool和SingleThreadExecutor：允许请求的队列长度为Integer.MAX_VALUE，可能堆积大量的请求，从而导致OOM
   - CachedThreadPool和ScheduledThreadPool：允许创建的线程数量为Integer.MAX_VALUE，可能会创建大量线程，导致OOM

   **方式一：通过构造方法实现**：

   ThreadPoolExecutor(int, int, long, TimeUnit,BlockingQueue<Runnable>, TheradFactory, RejectedExecutionHandler)

   **方式二：通过Executor框架的工具类Executors实现**（可以创建三种类型的ThreadPoolExecutor）：

   - FixedThreadPool：该方法返回一个固定线程数量的线程池。该线程池中的线程数量始终不变。当有一个新的任务提交时，线程池中若有空闲线程，则立即执行。若没有，则新任务暂存在一个任务队列中，等有线程空闲时，处理在任务队列中的任务。
   - SingleThreadExecutor：方法返回一个只有一个线程的线程池。若多于一个任务被提交到该线程池，保存在队列，线程空闲，执行队列中任务。
   - CachedThreadPool：该方法返回一个可根据实际情况调整线程数量的线程池。线程池的线程数量不确定，但若有空闲线程可以复用，则会优先使用可复用的线程。若所有线程都在工作，则创建新的线程处理任务。

4. **ThreadPoolExecutor构造函数重要参数分析**

   `ThreadPoolExecutor`3个重要参数：

   - **`corePoolSize`**：核心线程数定义了最小可以同时运行的线程数量
   - **`maximumPoolSize`**：当队列中存放的任务达到队列容量的时候，当前可以同时运行的线程数量变为最大线程数。
   - **`workQueue`**：当新任务来的时候会先判断当前运行的线程数量是否达到核心线程数，达到的话，新任务就会被存放在队列中。

   其他常见参数：

   - **`keepAliveTime`**：当线程池中的线程数量大于corePoolSize的时候，如果这时没有新任务提交，核心线程外的线程不会立刻销毁，而是会等待，直到时间超过keepAliveTime才会被回收
   - **`unit`**：keepAliveTime参数的时间单位
   - **`threadFactory`**：executor创建新线程的时候会用到
   - **`handler`**：饱和策略

5. **`ThreadPoolExecutor`饱和策略**

   如果当同时运行的线程数量达到最大线程数量并且队列也已经被放满了时，ThreadPoolTaskExecutor定义一些策略：

   - **`ThreadPoolExecutor.AbortPolicy`**:抛出RejectedExecutionException来拒绝新任务的处理。

   - **`ThreadPoolExecutor.CallerRunsPolicy`**：调用执行自己的线程运行任务。您不会任务请求。但是这种策略会降低对于新任务提交速度，影响程序的整体性能。另外，这个策略喜欢增加队列容量。如果您的应用程序可以承受此延迟并且你不能任务丢弃任何一个任务请求的话，你可以选择这个策略。

   - **`ThreadPoolExecutor.DiscardPolicy`：** 不处理新任务，直接丢弃掉。

   - **`ThreadPoolExecutor.DiscardOldestPolicy`：** 此策略将丢弃最早的未处理的任务请求。

     ![线程池处理流程](/Users/zhangyi/Desktop/Java复习图/线程池处理流程.png)

### 11.Atomic原子类

在java.utils.concurrent包里，有四种原子类：

1. 基本类型：
   - AtomicInteger
   - AtomicLong
   - AtomicBoolean
2. 数组类型：
   - AtomicIntegerArray
   - AtomicLongArray
   - AtomicReferenceArray
3. 引用类型
   - AtomicReference
   - AtomicStampedReference
   - AtomicMarkableReference
4. 对象的属性修改类型
   - AtomicIntegerFieldUpdater
   - AtomicLongFieldUpdater
   - AtomicReferenceFieldUpdater

**Unsafe类**

1. 用于Atomic类底层，Atomic原子类底层通过CAS保证原子性，Unsafe运用其中

2. ##### 内存管理：

   通过Unsafe类可以分配内存，可以释放内存；该部分包括了allocateMemory（分配内存）、reallocateMemory（重新分配内存）、copyMemory（拷贝内存）、freeMemory（释放内存 ）、getAddress（获取内存地址）等

3. ##### 可以定位对象某字段的内存位置，也可以修改对象的字段值，即使它是私有的；

4. ##### 挂起与恢复

   将一个线程进行挂起是通过park方法实现的，调用 park后，线程将一直阻塞直到超时或者中断等条件出现。unpark可以终止一个挂起的线程，使其恢复正常。整个并发框架中对

5. **多线程同步：**

   CAS操作：是通过compareAndSwapXXX方法实现的。

   CAS操作有3个操作数，内存值M，预期值E，新值U，如果M==E，则将内存值修改为B，否则啥都不做。



### 12.AQS (AbstractQueuedSynchronizer)

抽象队列同步器，在java.util.concurrent.locks包下。AQS 是一个用来构建锁和同步器的框架，使用 AQS 能简单且高效地构造出应用广泛的大量的同步器，比如`ReentrantLock`，`Semaphore`，其他的诸如 `ReentrantReadWriteLock`，`SynchronousQueue`，`FutureTask` 等等皆是基于 AQS 的。

**AQS定义两种资源共享方式**：

- Exclusive(独占)：只有一个线程能执行，如ReentrantLock。又分为公平锁和非公平锁:

  - 公平锁：按照线程在队列的排队顺序，先到者先拿到锁
  - 非公平锁：当线程要获取锁时，无视队列顺序直接去抢锁，谁抢到就是谁的

- Share(共享)：多个线程可同时执行，如CountDownLatch、Semaphore、CyclicBarrier、ReadWriteLock

  

**AQS中等待锁的线程队列时CLH队列**：

- 独占锁中竞争资源在一个时间点只能被一个线程锁访问；而其它线程则需要等待；
- 共享锁的竞争资源在一个时间点可以被多个线程锁访问

![CLH队列](/Users/zhangyi/Desktop/Java复习图/CLH队列.png)

AQS 使用一个 int 成员变量来表示同步状态，通过内置的 FIFO 队列来完成获取资源线程的排队工作。**AQS 使用 CAS 对该同步状态进行原子操作实现对其值的修改。**



**AQS底层使用了模版方法模式**：

同步器的设计时基于模版方法模式的，如果需要自定义同步器一般方式时：

1. 使用者继承AbstractQueuedSynchronizer并重写指定的方法。
2. 将AQS组合在自定义同步组件的实现中，并调用其模版方法，而这些模版方法会调用使用者重写的方法。

**AQS 使用了模板方法模式，自定义同步器时需要重写下面几个 AQS 提供的模板方法：**

```java
isHeldExclusively()//该线程是否正在独占资源。只有用到condition才需要去实现它。
tryAcquire(int)//独占方式。尝试获取资源，成功则返回true，失败则返回false。
tryRelease(int)//独占方式。尝试释放资源，成功则返回true，失败则返回false。
tryAcquireShared(int)//共享方式。尝试获取资源。负数表示失败；0表示成功，但没有剩余可用资源；正数表示成功，且有剩余资源。
tryReleaseShared(int)//共享方式。尝试释放资源，成功则返回true，失败则返回false。
```

默认情况下，每个方法都抛出 `UnsupportedOperationException`。 这些方法的实现必须是内部线程安全的，并且通常应该简短而不是阻塞。AQS 类中的其他方法都是 final ，所以无法被其他类使用，只有这几个方法可以被其他类使用。



**AQS组件总结：**

**CountDownLatch**

`CountDownLatch`是一个同步辅助类，在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待。

![CountDownLatch](/Users/zhangyi/Desktop/Java复习图/CountDownLatch.jpeg)

1.  CountDownLatch的作用是允许1或N个线程等待其他线程完成执行；而CyclicBarrier则是允许N个线程相互等待。
2. CountDownLatch的计数器无法被重置；CyclicBarrier的计数器可以被重置后使用，因此它被称为是循环的barrier。



**`CyclicBarrier`(循环栅栏)：**

**CyclicBarrier是一个同步辅助类，允许一组线程互相等待，直到到达某个公共屏障点 (common barrier point)。因为该 barrier 在释放等待线程后可以重用，所以称它为循环 的 barrier。**

![CyclicBarrier](/Users/zhangyi/Desktop/Java复习图/CyclicBarrier.jpeg)

 `CyclicBarrier` 和 `CountDownLatch` 非常类似，它也可以实现线程间的技术等待，但是它的功能比 `CountDownLatch` 更加复杂和强大。主要应用场景和 `CountDownLatch` 类似。`CyclicBarrier` 的字面意思是可循环使用（`Cyclic`）的屏障（`Barrier`）。它要做的事情是，让一组线程到达一个屏障（也可以叫同步点）时被阻塞，直到最后一个线程到达屏障时，屏障才会开门，所有被屏障拦截的线程才会继续干活。`CyclicBarrier` 默认的构造方法是 `CyclicBarrier(int parties)`，其参数表示屏障拦截的线程数量，每个线程调用 `await()` 方法告诉 `CyclicBarrier` 我已经到达了屏障，然后当前线程被阻塞。



**`Semaphore`(信号量)-允许多个线程同时访问：**

**Semaphore是一个计数信号量，它的本质是一个共享锁。**

![Semaphore](/Users/zhangyi/Desktop/Java复习图/Semaphore.jpeg)

信号量维护了一个信号量许可集。线程可以通过调用acquire()来获取信号量的许可；当信号量中有可用的许可时，线程能获取该许可；否则线程必须等待，直到有可用的许可为止。 线程可以通过release()来释放它所持有的信号量许可。

 `synchronized` 和 `ReentrantLock` 都是一次只允许一个线程访问某个资源，`Semaphore`(信号量)可以指定多个线程同时访问某个资源。

**"公平信号量"和"非公平信号量"**的释放信号量的机制是一样的！不同的是它们获取信号量的机制：线程在尝试获取信号量许可时，对于公平信号量而言，如果当前线程不在CLH队列的头部，则排队等候；而对于非公平信号量而言，无论当前线程是不是在CLH队列的头部，它都会直接获取信号量。该差异具体的体现在，它们的tryAcquireShared()函数的实现不同。





**AQS中的CAS：**

compareAndSetState()在AQS中实现。它的源码如下：

```java
protected final boolean compareAndSetState(int expect, int update) {
    return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
}
```

**说明**： compareAndSwapInt() 是sun.misc.Unsafe类中的一个本地方法。对此，我们需要了解的是 compareAndSetState(expect, update) 是以原子的方式操作当前线程；若当前线程的状态为expect，则设置它的状态为update。



### 13.公平锁与非公平锁

1. 公平锁与非公平锁：

   属于AQS独占锁，其中Sync类的子类**FairSync**和**NonFairSync**分别代表公平锁和非公平锁。ReentrantLock既可以公平也可以非公平：

   ![ReentrantLock结构](/Users/zhangyi/Desktop/Java复习图/ReentrantLock结构.jpeg)

2. Condition：

   Condition的作用是对锁进行更精确的控制。Condition中的**await()**方法相当于Object的**wait()**方法，Condition中的signal()方法相当于Object的**notify()**方法，Condition中的signalAll()相当于Object的**notifyAll()方法**。不同的是，Object中的**wait(), notify(), notifyAll()**方法是和**"同步锁"(synchronized关键字)**捆绑使用的；而Condition是需要与**"互斥锁"/"共享锁"**捆绑使用的。

3. LockSupport

   LockSupport是用来创建锁和其他同步类的基本线程阻塞原语。
   LockSupport中的**park() 和 unpark()** 的作用分别是阻塞线程和解除阻塞线程，而且park()和unpark()不会遇到“Thread.suspend 和 Thread.resume所可能引发的死锁”问题。
   因为park() 和 unpark()有许可的存在；调用 park() 的线程和另一个试图将其 unpark() 的线程之间的竞争将保持活性。

   

### 14.Callable和Future

1. **Callable：**

   Callable 是一个接口，它只包含一个call()方法。Callable是一个返回结果并且可能抛出异常的任务。

   为了便于理解，我们可以将Callable比作一个Runnable接口，而Callable的call()方法则类似于Runnable的run()方法。

   Callable的源码如下：

   ```java
   // 说明：从中我们可以看出Callable支持泛型。
   public interface Callable<V> {
       V call() throws Exception;
   }
   ```

2. **Future**

   Future 是一个接口。它用于表示异步计算的结果。提供了检查计算是否完成的方法，以等待计算的完成，并获取计算的结果。

   **Future**的源码如下：

   ```java
   //  Future用于表示异步计算的结果。它的实现类是FutureTask
   public interface Future<V> {
       // 试图取消对此任务的执行。
       boolean     cancel(boolean mayInterruptIfRunning)
       // 如果在任务正常完成前将其取消，则返回 true。
       boolean     isCancelled()
       // 如果任务已完成，则返回 true。
       boolean     isDone()
       // 如有必要，等待计算完成，然后获取其结果。
       V           get() throws InterruptedException, ExecutionException;
       // 如有必要，最多等待为使计算完成所给定的时间之后，获取其结果（如果结果可用）。
       V             get(long timeout, TimeUnit unit)
             throws InterruptedException, ExecutionException, TimeoutException;
   }
   ```

   ![Future](/Users/zhangyi/Desktop/Java复习图/Future.jpeg)

   **RunnableFuture是一个接口，它继承了Runnable和Future这两个接口。**RunnableFuture的源码如下：

   ```java
   public interface RunnableFuture<V> extends Runnable, Future<V> {
       void run();
   }
   ```

   **FutureTask实现了RunnableFuture接口。所以，我们也说它实现了Future接口。**

3. **submit()方法**

   ```java
   public <T> Future<T> submit(Callable<T> task) {
       if (task == null) throw new NullPointerException();
       // 创建一个RunnableFuture对象
       RunnableFuture<T> ftask = newTaskFor(task);
       // 执行“任务ftask”
       execute(ftask);
       // 返回“ftask”
       return ftask;
   }
   ```

   submit()通过newTaskFor(task)创建了**RunnableFuture**对象ftask。它的源码如下：

   ```java
   protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
       return new FutureTask<T>(callable);
   }
   ```

4. **FutureTask**

   ```java
   public FutureTask(Callable<V> callable) {
       if (callable == null)
           throw new NullPointerException();
       // callable是一个Callable对象
       this.callable = callable;
       // state记录FutureTask的状态
       this.state = NEW;       // ensure visibility of callable
   }
   ```



# JVM

### 1.Java内存区域

![Java运行时数据区域JDK1.8](/Users/zhangyi/Desktop/Java复习图/Java运行时数据区域JDK1.8.png)

**线程私有的：**

​	程序计数器、虚拟机栈、本地方法栈

**线程共享的：**

​	堆、方法区、直接内存

1. **程序计数器**

   程序计数器是一块较小的内存空间，可以看作当前线程执行字节码的行号指示器。**字节码解释器工作时通过改变这个计数器的值来选取下一条需要执行的字节码指令，分支、循环、跳转、异常处理、线程恢复等功能都需要依赖这个计数器来完成。**

   另外，**为了线程切换后能恢复到正确的执行位置，每条线程都需要有一个独立的程序计数器，各线程之间计数器互不影响，独立存储，我们称这类内存区域为“线程私有”的内存。**

   **从上面的介绍中我们知道程序计数器主要有两个作用：**

   1. 字节码解释器通过改变程序计数器来依次读取指令，从而实现代码的流程控制，如：顺序执行、选择、循环、异常处理。
   2. 在多线程的情况下，程序计数器用于记录当前线程执行的位置，从而当线程被切换回来的时候能够知道该线程上次运行到哪儿了。

   **注意：程序计数器是唯一一个不会出现 `OutOfMemoryError` 的内存区域，它的生命周期随着线程的创建而创建，随着线程的结束而死亡。**

2. **Java虚拟机栈**

   **与程序计数器一样，Java 虚拟机栈也是线程私有的，它的生命周期和线程相同，描述的是 Java 方法执行的内存模型，每次方法调用的数据都是通过栈传递的。**

   **Java 内存可以粗糙的区分为堆内存（Heap）和栈内存 (Stack),其中栈就是现在说的虚拟机栈，或者说是虚拟机栈中局部变量表部分。** （实际上，Java 虚拟机栈是由一个个栈帧组成，而每个栈帧中都拥有：**局部变量表、操作数栈、动态链接、返回地址**。）

   **局部变量表主要存放了编译期可知的各种数据类型**（boolean、byte、char、short、int、float、long、double）、**对象引用**（reference 类型，它不同于对象本身，可能是一个指向对象起始地址的引用指针，也可能是指向一个代表对象的句柄或其他与此对象相关的位置）。

   **Java 虚拟机栈会出现两种错误：`StackOverFlowError` 和 `OutOfMemoryError`。**

   - **`StackOverFlowError`：** 若 Java 虚拟机栈的内存大小不允许动态扩展，那么当线程请求栈的深度超过当前 Java 虚拟机栈的最大深度的时候，就抛出 StackOverFlowError 错误。
   - **`OutOfMemoryError`：** 若 Java 虚拟机堆中没有空闲内存，并且垃圾回收器也无法提供更多内存的话。就会抛出 OutOfMemoryError 错误。

   Java 虚拟机栈也是线程私有的，每个线程都有各自的 Java 虚拟机栈，而且随着线程的创建而创建，随着线程的死亡而死亡。

   **扩展：那么方法/函数如何调用？**

   Java 栈可用类比数据结构中栈，Java 栈中保存的主要内容是栈帧，每一次函数调用都会有一个对应的栈帧被压入 Java 栈，每一个函数调用结束后，都会有一个栈帧被弹出。

   Java 方法有两种返回方式：

   1. return 语句。
   2. 抛出异常。

   不管哪种返回方式都会导致栈帧被弹出。

3. **本地方法栈**

   和虚拟机栈所发挥的作用非常相似，区别是： **虚拟机栈为虚拟机执行 Java 方法 （也就是字节码）服务，而本地方法栈则为虚拟机使用到的 Native 方法服务。** 在 HotSpot 虚拟机中和 Java 虚拟机栈合二为一。

   本地方法被执行的时候，在本地方法栈也会创建一个栈帧，用于存放该本地方法的局部变量表、操作数栈、动态链接、出口信息。

   方法执行完毕后相应的栈帧也会出栈并释放内存空间，也会出现 `StackOverFlowError` 和 `OutOfMemoryError` 两种错误。

4. **堆**

   Java 虚拟机所管理的内存中最大的一块，Java 堆是所有线程共享的一块内存区域，在虚拟机启动时创建。**此内存区域的唯一目的就是存放对象实例，几乎所有的对象实例以及数组都在这里分配内存。**

   Java 堆是垃圾收集器管理的主要区域，因此也被称作**GC 堆（Garbage Collected Heap）**.从垃圾回收的角度，由于现在收集器基本都采用分代垃圾收集算法，所以 Java 堆还可以细分为：新生代和老年代：再细致一点有：Eden 空间、From Survivor、To Survivor 空间等。**进一步划分的目的是更好地回收内存，或者更快地分配内存。**

   在 JDK 7 版本及JDK 7 版本之前，堆内存被通常被分为下面三部分：

   1. 新生代内存(Young Generation)
   2. 老生代(Old Generation)
   3. 永生代(Permanent Generation)

   JDK 8 版本之后方法区（HotSpot 的永久代）被彻底移除了（JDK1.7 就已经开始了），取而代之是元空间，元空间使用的是直接内存。

   ![JVM堆内存](/Users/zhangyi/Desktop/Java复习图/JVM堆内存.png)

   **上图所示的 Eden 区、两个 Survivor 区都属于新生代（为了区分，这两个 Survivor 区域按照顺序被命名为 from 和 to），中间一层属于老年代。**

   大部分情况，对象都会首先在 Eden 区域分配，在一次新生代垃圾回收后，如果对象还存活，则会进入 s0 或者 s1，并且对象的年龄还会加 1(Eden 区->Survivor 区后对象的初始年龄变为 1)，当它的年龄增加到一定程度（默认为 15 岁），就会被晋升到老年代中。对象晋升到老年代的年龄阈值，可以通过参数 `-XX:MaxTenuringThreshold` 来设置。

   **修正（[issue552](https://github.com/Snailclimb/JavaGuide/issues/552)）：“Hotspot遍历所有对象时，按照年龄从小到大对其所占用的大小进行累积，当累积的某个年龄大小超过了survivor区的一半时，取这个年龄和MaxTenuringThreshold中更小的一个值，作为新的晋升年龄阈值”。**

   **堆这里最容易出现的就是 OutOfMemoryError 错误**，并且出现这种错误之后的表现形式还会有几种，比如：

   1. **`OutOfMemoryError: GC Overhead Limit Exceeded`** ： 当JVM花太多时间执行垃圾回收并且只能回收很少的堆空间时，就会发生此错误。
   2. **`java.lang.OutOfMemoryError: Java heap space`** :假如在创建新的对象时, 堆内存中的空间不足以存放新创建的对象, 就会引发`java.lang.OutOfMemoryError: Java heap space` 错误。(和本机物理内存无关，和你配置的内存大小有关！)

5. **方法区**

   方法区与 Java 堆一样，是各个线程共享的内存区域，它用于存储已被虚拟机加载的**类型信息**、**常量**、**静态变量**、即时编译器编译后的代码等数据。虽然 **Java 虚拟机规范把方法区描述为堆的一个逻辑部分**，但是它却有一个别名叫做 **Non-Heap（非堆）**，目的应该是与 Java 堆区分开来。

   - 方法区和永久代的关系：

     **方法区和永久代的关系很像 Java 中接口和类的关系，类实现了接口，而永久代就是 HotSpot 虚拟机对虚拟机规范中方法区的一种实现方式。** 也就是说，永久代是 HotSpot 的概念，方法区是 Java 虚拟机规范中的定义，是一种规范，而永久代是一种实现，一个是标准一个是实现，其他的虚拟机实现并没有永久代这一说法。

   - 常用参数

     ```java
     -XX:PermSize=N //方法区 (永久代) 初始大小 
     
     -XX:MaxPermSize=N //方法区 (永久代) 最大大小,超过这个值将会抛出 OutOfMemoryError 异常:java.lang.OutOfMemoryError: PermGen
     ```

     JDK 1.8 的时候，方法区（HotSpot 的永久代）被彻底移除了（JDK1.7 就已经开始了），取而代之是**元空间**，**元空间使用的是直接内存**。

   - #####  为什么要将永久代 (PermGen) 替换为元空间 (MetaSpace) 呢?

     1. 整个永久代有一个 JVM 本身设置固定大小上限，无法进行调整，而元空间使用的是直接内存，受本机可用内存的限制，虽然元空间仍旧可能溢出，但是比原来出现的几率会更小。

        当元空间溢出时会得到如下错误： `java.lang.OutOfMemoryError: MetaSpace`

     2. 元空间里面存放的是**类的元数据**，这样加载多少类的元数据就不由 `MaxPermSize` 控制了, 而由系统的实际可用空间来控制，这样能加载的类就更多了

     3. 在 JDK8，合并 HotSpot 和 JRockit 的代码时, JRockit 从来没有一个叫永久代的东西, 合并之后就没有必要额外的设置这么一个永久代的地方了。

6. **运行时常量池**

   运行时常量池是方法区的一部分。Class 文件中除了有类的版本、字段、方法、接口等描述信息外，还有**常量池表**（用于存放编译期生成的各种字面量和符号引用）,这部分内容将在类加载后放到运行时常量池。

   既然运行时常量池是方法区的一部分，自然受到方法区内存的限制，当常量池无法再申请到内存时会抛出 OutOfMemoryError 错误。

   1. **JDK1.7之前运行时常量池逻辑包含字符串常量池存放在方法区, 此时hotspot虚拟机对方法区的实现为永久代**
   2. **JDK1.7 字符串常量池被从方法区拿到了堆中, 这里没有提到运行时常量池,也就是说字符串常量池被单独拿到堆,运行时常量池剩下的东西还在方法区, 也就是hotspot中的永久代** 。
   3. **JDK1.8 hotspot移除了永久代用元空间(Metaspace)取而代之, 这时候字符串常量池还在堆, 运行时常量池还在方法区, 只不过方法区的实现从永久代变成了元空间(Metaspace)**

   **运行时常量池中，引用类型常量（例如CONSTANT_String、CONSTANT_Class、CONSTANT_MethodHandle、CONSTANT_MethodType之类）都存的是引用，实际的对象还是存在Java heap上的**

7. **直接内存**

   - **直接内存并不是虚拟机运行时数据区的一部分，也不是虚拟机规范中定义的内存区域，但是这部分内存也被频繁地使用。而且也可能导致 OutOfMemoryError 错误出现。**
   - JDK1.4 中新加入的 **NIO(New Input/Output) 类**，引入了一种基于**通道（Channel）** 与**缓存区（Buffer）** 的 I/O 方式，它可以直接使用 Native 函数库直接分配堆外内存，然后通过一个存储在 Java 堆中的 DirectByteBuffer 对象作为这块内存的引用进行操作。这样就能在一些场景中显著提高性能，因为**避免了在 Java 堆和 Native 堆之间来回复制数据**。
   - 本机直接内存的分配不会受到 Java 堆的限制，但是，既然是内存就会受到本机总内存大小以及处理器寻址空间的限制。

### 2.Java对象的创建过程

![Java创建对象的过程](/Users/zhangyi/Desktop/Java复习图/Java创建对象的过程.png)

1. 类加载检查：

   虚拟机遇到一条 new 指令时，首先将去检查这个指令的参数是否能在**常量池中**定位到这个类的**符号引用**，并且检查这个符号引用代表的**类是否已被加载过、解析和初始化**过。如果没有，那必须先执行相应的类加载过程。

2. 分配内存

   在**类加载检查**通过后，接下来虚拟机将为新生对象**分配内存**。

   对象所需的内存大小在类加载完成后便可确定，为对象分配空间的任务等同于把一块确定大小的内存从 Java 堆中划分出来。**分配方式**有 **“指针碰撞”** 和 **“空闲列表”** 两种，**选择哪种分配方式由 Java 堆是否规整决定，而 Java 堆是否规整又由所采用的垃圾收集器是否带有压缩整理功能决定**。

   **内存分配的两种方式：**

   1. **指针碰撞：**

      适用**堆内存规整**的情况，堆内使用**“标记-整理”**算法

      原理：用过的内存全部整合到一边，没用过的放在另一边，中间有个分界指针，只需要向着没用过的内存方向将该指针移动对象内存大小位置即可。

      适用GC收集器：Serial、ParNew

   2. **空闲列表：**

      适用于**堆内存不规整**的情况，堆内使用**“标记-清除”**算法

      原理：虚拟机会维护一个列表，列表中记录哪些内存块是可用的，在分配时找一块足够大的内存块划分给实例，最后更新列表记录。

      适用GC收集器：CMS

   ![内存分配的两种方式](/Users/zhangyi/Desktop/Java复习图/内存分配的两种方式.png)

   选择哪一种，取决于 Java 堆内存是否规整。而 Java 堆内存是否规整，取决于 GC 收集器的算法是"标记-清除"，还是"标记-整理"，值得注意的是，复制算法内存也是规整的

   **内存分配并发问题**:

   在创建对象的时候有一个很重要的问题，就是线程安全，因为在实际开发过程中，创建对象是很频繁的事情，作为虚拟机来说，必须要保证线程是安全的，通常来讲，虚拟机采用两种方式来保证线程安全：

   - **CAS+失败重试：** CAS 是乐观锁的一种实现方式。所谓乐观锁就是，每次不加锁而是假设没有冲突而去完成某项操作，如果因为冲突失败就重试，直到成功为止。**虚拟机采用 CAS 配上失败重试的方式保证更新操作的原子性。**
   - **TLAB：** 为每一个线程预先在 Eden 区分配一块儿内存，JVM 在给线程中的对象分配内存时，首先在 TLAB 分配，当对象大于 TLAB 中的剩余内存或 TLAB 的内存已用尽时，再采用上述的 CAS 进行内存分配

3. 初始化零值

   内存分配完成后，虚拟机需要将分配到的内存空间都初始化为零值（不包括对象头），这一步操作保证了对象的实例字段在 Java 代码中可以不赋初始值就直接使用，程序能访问到这些字段的数据类型所对应的零值。

4. 设置对象头

   初始化零值完成之后，**虚拟机要对对象进行必要的设置**，例如这个对象是哪个类的实例、如何才能找到类的元数据信息、对象的哈希码、对象的 GC 分代年龄等信息。 **这些信息存放在对象头中。** 另外，根据虚拟机当前运行状态的不同，如是否启用偏向锁等，对象头会有不同的设置方式。

5. 执行init方法

   在上面工作都完成之后，从虚拟机的视角来看，一个新的对象已经产生了，但从 Java 程序的视角来看，对象创建才刚开始，`<init>` 方法还没有执行，所有的字段都还为零。所以一般来说，执行 new 指令之后会接着执行 `<init>` 方法，把对象按照程序员的意愿进行初始化，这样一个真正可用的对象才算完全产生出来。

### 3.对象的访问定位的两种方式

建立对象就是为了使用对象，Java程序通过栈上的 reference 数据来操作堆上的具体对象。对象的访问方式由虚拟机实现而定，目前主流的访问方式有**①使用句柄**和**②直接指针**两种：

1. **句柄：** 如果使用句柄的话，那么Java堆中将会划分出一块内存来作为句柄池，reference 中存储的就是对象的句柄地址，而句柄中包含了对象实例数据与类型数据各自的具体地址信息

   ![句柄访问](/Users/zhangyi/Desktop/Java复习图/句柄访问.png)

2. **直接指针：** 如果使用直接指针访问，那么 Java 堆对象的布局中就必须考虑如何放置访问类型数据的相关信息，而reference 中存储的直接就是对象的地址。

   ![直接指针访问](/Users/zhangyi/Desktop/Java复习图/直接指针访问.png)

**这两种对象访问方式各有优势。使用句柄来访问的最大好处是 reference 中存储的是稳定的句柄地址，在对象被移动时只会改变句柄中的实例数据指针，而 reference 本身不需要修改。使用直接指针访问方式最大的好处就是速度快，它节省了一次指针定位的时间开销。**

### 4.JVM内存分配及回收

Java 的自动内存管理主要是针对对象内存的回收和对象内存的分配。同时，Java 自动内存管理最核心的功能是 **堆** 内存中对象的分配与回收。

Java 堆是垃圾收集器管理的主要区域，因此也被称作**GC 堆（Garbage Collected Heap）**.从垃圾回收的角度，由于现在收集器基本都采用分代垃圾收集算法，所以 Java 堆还可以细分为：新生代和老年代：再细致一点有：Eden 空间、From Survivor、To Survivor 空间等。**进一步划分的目的是更好地回收内存，或者更快地分配内存。**

**堆空间的基本结构：**

![堆内存结构](/Users/zhangyi/Desktop/Java复习图/堆内存结构.png)

上图所示的 Eden 区、From Survivor0("From") 区、To Survivor1("To") 区都属于新生代，Old Memory 区属于老年代。

大部分情况，对象都会首先在 Eden 区域分配，在一次新生代垃圾回收后，如果对象还存活，则会进入 s0 或者 s1，并且对象的年龄还会加 1(Eden 区->Survivor 区后对象的初始年龄变为 1)，当它的年龄增加到一定程度（默认为 15 岁），就会被晋升到老年代中。对象晋升到老年代的年龄阈值，可以通过参数 `-XX:MaxTenuringThreshold` 来设置。

**修正（[issue552](https://github.com/Snailclimb/JavaGuide/issues/552)）：“Hotspot 遍历所有对象时，按照年龄从小到大对其所占用的大小进行累积，当累积的某个年龄大小超过了 survivor 区的一半时，取这个年龄和 MaxTenuringThreshold 中更小的一个值，作为新的晋升年龄阈值”。**

经过这次 GC 后，Eden 区和"From"区已经被清空。这个时候，"From"和"To"会交换他们的角色，也就是新的"To"就是上次 GC 前的“From”，新的"From"就是上次 GC 前的"To"。不管怎样，都会保证名为 To 的 Survivor 区域是空的。Minor GC 会一直重复这样的过程，直到“To”区被填满，"To"区被填满之后，会将所有对象移动到老年代中。

### 5.堆内存中对象的分配策略

![堆内存中对象的分配策略](/Users/zhangyi/Desktop/Java复习图/堆内存中对象的分配策略.png)

1. **对象优先在Eden区分配**

   目前主流的垃圾收集器都会采用分代回收算法，因此需要将堆内存分为新生代和老年代，这样我们就可以根据各个年代的特点选择合适的垃圾收集算法。

   大多数情况下，对象在新生代中 eden 区分配。当 eden 区没有足够空间进行分配时，虚拟机将发起一次 Minor GC.

   当 Eden 区没有足够空间进行分配时，虚拟机将发起一次 Minor GC。GC 期间虚拟机又发现 对象无法存入 Survivor 空间，所以只好通过 **分配担保机制** 把新生代的对象提前转移到老年代中去，老年代上的空间足够存放对象，所以不会出现 Full GC。执行 Minor GC 后，后面分配的对象如果能够存在 eden 区的话，还是会在 eden 区分配内存。

2. **大对象直接进入老年代**

   大对象就是需要大量连续内存空间的对象（比如：字符串、数组）。

   **为什么要这样呢？**

   为了避免为大对象分配内存时由于分配担保机制带来的复制而降低效率。

3. **长期存活的对象进入老年代**

   既然虚拟机采用了分代收集的思想来管理内存，那么内存回收时就必须能识别哪些对象应放在新生代，哪些对象应放在老年代中。为了做到这一点，虚拟机给每个对象一个对象年龄（Age）计数器。

   如果对象在 Eden 出生并经过第一次 Minor GC 后仍然能够存活，并且能被 Survivor 容纳的话，将被移动到 Survivor 空间中，并将对象年龄设为 1.对象在 Survivor 中每熬过一次 MinorGC,年龄就增加 1 岁，当它的年龄增加到一定程度（默认为 15 岁），就会被晋升到老年代中。对象晋升到老年代的年龄阈值，可以通过参数 `-XX:MaxTenuringThreshold` 来设置。

4. 动态对象年龄判定

   大部分情况，对象都会首先在 Eden 区域分配，在一次新生代垃圾回收后，如果对象还存活，则会进入 s0 或者 s1，并且对象的年龄还会加 1(Eden 区->Survivor 区后对象的初始年龄变为 1)，当它的年龄增加到一定程度（默认为 15 岁），就会被晋升到老年代中。对象晋升到老年代的年龄阈值，可以通过参数 `-XX:MaxTenuringThreshold` 来设置。

   **修正（[issue552](https://github.com/Snailclimb/JavaGuide/issues/552)）：“Hotspot 遍历所有对象时，按照年龄从小到大对其所占用的大小进行累积，当累积的某个年龄大小超过了 survivor 区的一半时，取这个年龄和 MaxTenuringThreshold 中更小的一个值，作为新的晋升年龄阈值”。**

   ```c++
   uint ageTable::compute_tenuring_threshold(size_t survivor_capacity) {    //survivor_capacity是survivor空间的大小 
   	size_t desired_survivor_size = (size_t)((((double) 	survivor_capacity)*TargetSurvivorRatio)/100); 
   	size_t total = 0; uint age = 1; 
    	while (age < table_size) { 
       total += sizes[age];
       //sizes数组是每个年龄段对象大小 
       if (total > desired_survivor_size) break; 
       age++; 
     } 
     uint result = age < MaxTenuringThreshold ? age :MaxTenuringThreshold;    ... }
   ```

   额外补充说明([issue672](https://github.com/Snailclimb/JavaGuide/issues/672))：**关于默认的晋升年龄是 15，这个说法的来源大部分都是《深入理解 Java 虚拟机》这本书。** 如果你去 Oracle 的官网阅读[相关的虚拟机参数](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/java.html)，你会发现`-XX:MaxTenuringThreshold=threshold`这里有个说明.

   **默认晋升年龄并不都是 15，这个是要区分垃圾收集器的，CMS 就是 6.**

   

### 6.主要进行GC的区域

针对 HotSpot VM 的实现，它里面的 GC 其实准确分类只有两大种：

部分收集 (Partial GC)：

- 新生代收集（Minor GC / Young GC）：只对新生代进行垃圾收集；
- 老年代收集（Major GC / Old GC）：只对老年代进行垃圾收集。需要注意的是 Major GC 在有的语境中也用于指代整堆收集；
- 混合收集（Mixed GC）：对整个新生代和部分老年代进行垃圾收集。

整堆收集 (Full GC)：收集整个 Java 堆和方法区。

![主要进行GC的区域](/Users/zhangyi/Desktop/Java复习图/主要进行GC的区域.png)

### 7.**如何判断对象是否死亡？**

堆中几乎放着所有的对象实例，对堆垃圾回收前的第一步就是要判断哪些对象已经死亡（即不能再被任何途径使用的对象）。

1. **引用计数法**

   给对象中添加一个引用计数器，每当有一个地方引用它，计数器就加1；当引用失效，计数器就减1；任何时候计数器为0的对象就是不可能再被使用的。但是有很多例外情况，比如很难解决循环引用的问题

2. **可达性分析算法**

   这个算法的基本思想就是通过一系列的称为 **“GC Roots”** 的对象作为起点，从这些节点开始向下搜索，节点所走过的路径称为引用链，当一个对象到 GC Roots 没有任何引用链相连的话，则证明此对象是不可用的。当GC Roots到对象不可达，此对象不可能再被使用。

   GC Roots包括的对象：1.虚拟机栈中引用的对象；2.方法区中类静态属性引用的对象；3.方法区中常量引用的对象；4.本地方法栈中Native方法引用的对象；5.Java虚拟机内部的引用；6.所有被同步锁(synchronized)持有的对象；

   ![可达性分析算法](/Users/zhangyi/Desktop/Java复习图/可达性分析算法.jpeg)

### 8.强引用、软、弱、虚引用

无论是通过引用计数法判断对象引用数量，还是通过可达性分析法判断对象的引用链是否可达，判定对象的存活都与“引用”有关。

JDK1.2之前，Java中引用的定义很传统：如果reference类型的数据存储的数值代表的是另一块内存的起始地址，就称这块内存代表一个引用。

JDK1.2以后，Java对引用的概念进行了扩充，将引用分为强引用、软引用、弱引用、虚引用四种（引用强度逐渐减弱）

1. **强引用：**

   最传统的“引用”定义，指程序代码中普遍存在的引用赋值，即类似“Object obj = new Object()”；**只要强引用关系存在，GC永远不会回收被引用的对象。**

2. **软引用**

   描述还有用但非必须的对象。被软引用关联的对象，在系统内存空间足够时，垃圾回收器就不会回收它，如果内存空间不足了，就会回收这些对象的内存。如果回收后内存还不够，OOMError。

   软引用可以和一个引用队列（ReferenceQueue）联合使用，如果软引用所引用的对象被垃圾回收，JAVA虚拟机就会把这个软引用加入到与之关联的引用队列中。

3. **弱引用**

   被弱引用关联的对象**只能生存到下一次垃圾回收**，当GC开始工作，不论内存多少，都回收弱引用关联的对象。

4. **虚引用**

   **虚引用主要用来跟踪对象被垃圾回收的活动**。不会对生存时间构成影响，也无法通过其获得对象实例。目的只为了被GC回收时收到系统通知。

   **虚引用与软引用和弱引用的一个区别在于：** 虚引用必须和引用队列（ReferenceQueue）联合使用。当垃 圾回收器准备回收一个对象时，如果发现它还有虚引用，就会在回收对象的内存之前，把这个虚引用加入到与之关联的引用队列中。程序可以通过判断引用队列中是 否已经加入了虚引用，来了解被引用的对象是否将要被垃圾回收。程序如果发现某个虚引用已经被加入到引用队列，那么就可以在所引用的对象的内存被回收之前采取必要的行动。

   特别注意，在程序设计中一般很少使用弱引用与虚引用，使用软引用的情况较多，这是因为**软引用可以加速JVM对垃圾内存的回收速度，可以维护系统的运行安全，防止内存溢出（OutOfMemory）等问题的产生**。

### **9.如何判断一个常量是废弃常量**

运行时常量池主要回收的是废弃的常量。那么，我们如何判断一个常量是废弃常量呢？

**当没有任何对象引用常量池中常量，则说明其是废弃常量。**

假如在常量池中存在字符串 "abc"，如果当前没有任何String对象引用该字符串常量的话，就说明常量 "abc" 就是废弃常量，如果这时发生内存回收的话而且有必要的话，"abc" 就会被系统清理出常量池。

### 10.如何判断一个类是无用的类

方法区主要回收的是无用的类，那么如何判断一个类是无用的类的呢？

需要同时满足下面 3 个条件才能算是 **“无用的类”** ：

- 该类所有的实例都已经被回收，也就是 Java 堆中不存在该类的任何实例。
- 加载该类的 `ClassLoader` 已经被回收。
- 该类对应的 `java.lang.Class` 对象没有在任何地方被引用，无法在任何地方通过反射访问该类的方法。

虚拟机可以对满足上述 3 个条件的无用类进行回收，这里说的仅仅是“可以”，而并不是和对象一样不使用了就会必然被回收。

### 11.垃圾回收有哪些算法？各自的特点？

1. **标记-清除算法**

   该算法分为“标记”和“清除”阶段：首先标记出所有不需要回收的对象，在标记完成后统一回收掉所有没有被标记的对象。它是最基础的收集算法，后续的算法都是对其不足进行改进得到。这种垃圾收集算法会带来两个明显的问题：

   1. **效率问题**
   2. **空间问题（标记清除后会产生大量不连续的碎片）**

   ![标记清除](/Users/zhangyi/Desktop/Java复习图/标记清除.jpeg)

   

2. **标记-复制算法**

   为了解决效率问题，“复制”收集算法出现了。它可以将内存分为大小相同的两块，每次使用其中的一块。当这一块的内存使用完后，就将还存活的对象复制到另一块去，然后再把使用的空间一次清理掉。这样就使每次的内存回收都是对内存区间的一半进行回收。

   ![标记复制](/Users/zhangyi/Desktop/Java复习图/标记复制.jpeg)

   

3. **标记-整理算法**

   根据老年代的特点提出的一种标记算法，标记过程仍然与“标记-清除”算法一样，但后续步骤不是直接对可回收对象回收，而是让所有存活的对象向一端移动，然后直接清理掉端边界以外的内存。

   ![标记整理](/Users/zhangyi/Desktop/Java复习图/标记整理.jpeg)

   

4. **分代收集算法**

   当前虚拟机的垃圾收集都采用分代收集算法，这种算法没有什么新的思想，只是根据对象存活周期的不同将内存分为几块。一般将 java 堆分为新生代和老年代，这样我们就可以根据各个年代的特点选择合适的垃圾收集算法。

   **比如在新生代中，每次收集都会有大量对象死去，所以可以选择复制算法，只需要付出少量对象的复制成本就可以完成每次垃圾收集。而老年代的对象存活几率是比较高的，而且没有额外的空间对它进行分配担保，所以我们必须选择“标记-清除”或“标记-整理”算法进行垃圾收集。**

### 12.HotSpot为什么分新生代和老年代？

**主要是为了提升 GC 效率。**



### 13.常见的垃圾回收器

![常见的垃圾回收器](/Users/zhangyi/Desktop/Java复习图/常见的垃圾回收器.jpeg)

1. **Serial收集器**

   Serial（串行）收集器是最基本、历史最悠久的垃圾收集器了。大家看名字就知道这个收集器是一个单线程收集器了。它的 **“单线程”** 的意义不仅仅意味着它只会使用一条垃圾收集线程去完成垃圾收集工作，更重要的是它在进行垃圾收集工作的时候必须暂停其他所有的工作线程（ **"Stop The World"** ），直到它收集结束。

   **新生代采用复制算法，老年代采用标记-整理算法。**

   ![Serial收集器](/Users/zhangyi/Desktop/Java复习图/Serial收集器.jpeg)

   Serial 收集器优点是它**简单而高效（与其他收集器的单线程相比）**。Serial 收集器由于没有线程交互的开销，自然可以获得很高的单线程收集效率。Serial 收集器对于运行在 Client 模式下的虚拟机来说是个不错的选择。

   

2. **ParNew收集器**

   **ParNew 收集器其实就是 Serial 收集器的多线程版本，除了使用多线程进行垃圾收集外，其余行为（控制参数、收集算法、回收策略等等）和 Serial 收集器完全一样。**

   **新生代采用复制算法，老年代采用标记-整理算法。**

   ![ParNew收集器](/Users/zhangyi/Desktop/Java复习图/ParNew收集器.jpeg)

   它是许多运行在 Server 模式下的虚拟机的首要选择，除了 Serial 收集器外，只有它能与 CMS 收集器（真正意义上的并发收集器，后面会介绍到）配合工作。

   **并行和并发概念补充：**

   - **并行（Parallel）** ：指多条垃圾收集线程并行工作，但此时用户线程仍然处于等待状态。
   - **并发（Concurrent）**：指用户线程与垃圾收集线程同时执行（但不一定是并行，可能会交替执行），用户程序在继续运行，而垃圾收集器运行在另一个 CPU 上。

   

3. **Parallel Scavenge收集器**

   Parallel Scavenge 收集器也是使用复制算法的多线程收集器，它看上去几乎和 ParNew 都一样。 **那么它有什么特别之处呢？**

   **Parallel Scavenge 收集器关注点是吞吐量（高效率的利用 CPU）。CMS 等垃圾收集器的关注点更多的是用户线程的停顿时间（提高用户体验）。所谓吞吐量就是 CPU 中用于运行用户代码的时间与 CPU 总消耗时间的比值。** Parallel Scavenge 收集器提供了很多参数供用户找到最合适的停顿时间或最大吞吐量，如果对于收集器运作不太了解，手工优化存在困难的时候，使用 Parallel Scavenge 收集器配合自适应调节策略，把内存管理优化交给虚拟机去完成也是一个不错的选择。

   **新生代采用复制算法，老年代采用标记-整理算法。**

   ![Parallel Scavenge收集器](/Users/zhangyi/Desktop/Java复习图/Parallel Scavenge收集器.jpeg)

   **这是 JDK1.8 默认收集器**

   JDK1.8 默认使用的是 Parallel Scavenge + Parallel Old，如果指定了-XX:+UseParallelGC 参数，则默认指定了-XX:+UseParallelOldGC，可以使用-XX:-UseParallelOldGC 来禁用该功能

   

4. **Serial Old收集器**

   **Serial 收集器的老年代版本**，它同样是一个单线程收集器。它主要有两大用途：一种用途是在 JDK1.5 以及以前的版本中与 Parallel Scavenge 收集器搭配使用，另一种用途是作为 CMS 收集器的后备方案。

   

5. **Parallel Old收集器**

   **Parallel Scavenge 收集器的老年代版本**。使用多线程和“标记-整理”算法。在注重吞吐量以及 CPU 资源的场合，都可以优先考虑 Parallel Scavenge 收集器和 Parallel Old 收集器。

   

6. **CMS收集器**

   **CMS（Concurrent Mark Sweep）收集器是一种以获取最短回收停顿时间为目标的收集器。它非常符合在注重用户体验的应用上使用。**

   **CMS（Concurrent Mark Sweep）收集器是 HotSpot 虚拟机第一款真正意义上的并发收集器，它第一次实现了让垃圾收集线程与用户线程（基本上）同时工作。**

   从名字中的**Mark Sweep**这两个词可以看出，CMS 收集器是一种 **“标记-清除”算法**实现的，它的运作过程相比于前面几种垃圾收集器来说更加复杂一些。整个过程分为四个步骤：

   - **初始标记：** 暂停所有的其他线程，并记录下直接与 root 相连的对象，速度很快 ；
   - **并发标记：** 同时开启 GC 和用户线程，用一个闭包结构去记录可达对象。但在这个阶段结束，这个闭包结构并不能保证包含当前所有的可达对象。因为用户线程可能会不断的更新引用域，所以 GC 线程无法保证可达性分析的实时性。所以这个算法里会跟踪记录这些发生引用更新的地方。
   - **重新标记：** 重新标记阶段就是为了修正并发标记期间因为用户程序继续运行而导致标记产生变动的那一部分对象的标记记录，这个阶段的停顿时间一般会比初始标记阶段的时间稍长，远远比并发标记阶段时间短
   - **并发清除：** 开启用户线程，同时 GC 线程开始对未标记的区域做清扫。

   ![CMS收集器](/Users/zhangyi/Desktop/Java复习图/CMS收集器.jpeg)

   从它的名字就可以看出它是一款优秀的垃圾收集器，主要优点：**并发收集、低停顿**。但是它有下面三个明显的缺点：

   - **对 CPU 资源敏感；**
   - **无法处理浮动垃圾；**
   - **它使用的回收算法-“标记-清除”算法会导致收集结束时会有大量空间碎片产生。**

   

7. **G1收集器**

   **G1 (Garbage-First) 是一款面向服务器的垃圾收集器,主要针对配备多颗处理器及大容量内存的机器. 以极高概率满足 GC 停顿时间要求的同时,还具备高吞吐量性能特征.**

   被视为 JDK1.7 中 HotSpot 虚拟机的一个重要进化特征。它具备一下特点：

   - **并行与并发**：G1 能充分利用 CPU、多核环境下的硬件优势，使用多个 CPU（CPU 或者 CPU 核心）来缩短 Stop-The-World 停顿时间。部分其他收集器原本需要停顿 Java 线程执行的 GC 动作，G1 收集器仍然可以通过并发的方式让 java 程序继续执行。
   - **分代收集**：虽然 G1 可以不需要其他收集器配合就能独立管理整个 GC 堆，但是还是保留了分代的概念。
   - **空间整合**：与 CMS 的“标记--清理”算法不同，G1 从整体来看是基于“标记整理”算法实现的收集器；从局部上来看是基于“复制”算法实现的。
   - **可预测的停顿**：这是 G1 相对于 CMS 的另一个大优势，降低停顿时间是 G1 和 CMS 共同的关注点，但 G1 除了追求低停顿外，还能建立可预测的停顿时间模型，能让使用者明确指定在一个长度为 M 毫秒的时间片段内。

   G1 收集器的运作大致分为以下几个步骤：

   - **初始标记**
   - **并发标记**
   - **最终标记**
   - **筛选回收**

   **G1 收集器在后台维护了一个优先列表，每次根据允许的收集时间，优先选择回收价值最大的 Region(这也就是它的名字 Garbage-First 的由来)**。这种使用 Region 划分内存空间以及有优先级的区域回收方式，保证了 G1 收集器在有限时间内可以尽可能高的收集效率（把内存化整为零）。

   

8. **ZGC收集器**

   与 CMS 中的 ParNew 和 G1 类似，ZGC 也采用标记-复制算法，不过 ZGC 对该算法做了重大改进。

   在 ZGC 中出现 Stop The World 的情况会更少！

   详情可以看 ： [《新一代垃圾回收器 ZGC 的探索与实践》](https://tech.meituan.com/2020/08/06/new-zgc-practice-in-meituan.html)

### 14.类加载器和双亲委派

自带三个类加载器：

1. **启动类加载器(Bootstrap Classloader)**
2. **扩展类加载器(Extension Classloader)**
3. **应用程序类加载器(Application Classloader)**
4. **剩下为自定义的类加载器**

![类加载器关系](/Users/zhangyi/Desktop/Java复习图/类加载器关系.png)

1. **双亲委派机制：**

   一个类加载器收到类加载任务时，先交给父加载器，父加载器无法完成，回到自己。所以首先都交给启动类加载器，不能执行再下分。

2. **双亲委派机制的好处：**

   1. **避免重复加载，父类加载完子类不需要再加载**
   2. **更安全，解决了个类加载器基础类统一的问题**

3. **破坏双亲委派机制：**

   1. 在双亲委派模型出现之前-----即JDK1.2发布之前。
   2. 基础类调用会用户的代码时。**线程上下文类加载器（Thread Context ClassLoader）**。这个类加载器可以通过java.lang.Thread类的setContextClassLoader方法进行设置。如果创建线程时还未设置，它将会从父线程中继承一个，如果在应用程序的全局范围内都没有设置过多的话，那这个类加载器默认即使应用程序类加载器。
   3. 为了实现热插拔，热部署，模块化，意思是添加一个功能或减去一个功能不用重启，只需要把这模块连同类加载器一起换掉就实现了代码的热替换。

4. **TomCat使用默认的类加载机制会遇到的问题：**

   1. 一个web容器可能需要部署两个应用程序，不同的应用程序可能会依赖**同一个第三方类库的不同版本**，不能要求同一个类库在同一个服务器只有一份，因此要保证每个应用程序的类库都是独立的，保证相互隔离。
   2. 部署在同一个web容器中相同的类库相同的版本可以共享。否则，如果服务器有10个应用程序，那么要有10份相同的类库加载进虚拟机，这是扯淡的。
   3. web容器也有自己依赖的类库，不能于应用程序的类库混淆。基于安全考虑，应该让容器的类库和程序的类库隔离开来。
   4. web容器要支持jsp的修改，我们知道，jsp 文件最终也是要编译成class文件才能在虚拟机中运行，但程序运行后修改jsp已经是司空见惯的事情，否则要你何用？ 所以，web容器需要支持 jsp 修改后不用重启。

5. **为什么Tomcat 不能使用默认的类加载机制？**

   1. 第一个问题，如果使用默认的类加载器机制，那么是无法加载两个相同类库的不同版本的，默认的累加器是不管你是什么版本的，只在乎你的全限定类名，并且只有一份。
   2. 第二个问题，默认的类加载器是能够实现的，因为他的职责就是保证唯一性。
   3. 第三个问题和第一个问题一样。
   4. 我们再看第四个问题，我们想我们要怎么实现jsp文件的热修改（楼主起的名字），jsp 文件其实也就是class文件，那么如果修改了，但类名还是一样，类加载器会直接取方法区中已经存在的，修改后的jsp是不会重新加载的。那么怎么办呢？我们可以直接卸载掉这jsp文件的类加载器，所以你应该想到了，每个jsp文件对应一个唯一的类加载器，当一个jsp文件修改了，就直接卸载这个jsp类加载器。重新创建类加载器，重新加载jsp文件。

6. ##### Tomcat 如何实现自己独特的类加载机制

   ![Tomcat类加载机制](/Users/zhangyi/Desktop/Java复习图/Tomcat类加载机制.png)

   从上图看出：

   1. CommonClassLoader能加载的类都可以被Catalina ClassLoader和SharedClassLoader使用，从而实现了公有类库的共用，而CatalinaClassLoader和Shared ClassLoader自己能加载的类则与对方相互隔离。
   2. WebAppClassLoader可以使用SharedClassLoader加载到的类，但各个WebAppClassLoader实例之间相互隔离。
   3. 而JasperLoader的加载范围仅仅是这个JSP文件所编译出来的那一个.Class文件，它出现的目的就是为了被丢弃：当Web容器检测到JSP文件被修改时，会替换掉目前的JasperLoader的实例，并通过再建立一个新的Jsp类加载器来实现JSP文件的HotSwap功能。
   4. tomcat 为了实现隔离性，没有遵守双亲委派，每个webappClassLoader加载自己的目录下的class文件，不会传递给父类加载器。

   ##### 如果tomcat 的 Common ClassLoader 想加载 WebApp ClassLoader 中的类，该怎么办？

   我们可以使用线程上下文类加载器实现，使用线程上下文加载器，可以让父类加载器请求子类加载器去完成类加载的动作。



### 15.类加载机制

**加载 --> 连接(验证 --> 准备 --> 解析) --> 初始化 --> 使用 --> 卸载**

1. **加载：完成三件事**

   1. 通过一个类的全限定名获取定义此类的二进制字节流
   2. 将这个字节流代表的静态结构转为方法区运行时的结构
   3. 在内存中生成java.lang.Class

2. **验证：作用是确保被加载的类的正确性**

   1. 文件格式验证
   2. 元数据验证
   3. 字节码验证
   4. 符号引用验证

3. **准备：为类变量分配内存并设置初始值**

   1. 类变量（static）会分配内存，实例变量不会
   2. 初始值为默认值，不是代码中被显示赋的值

4. **解析：虚拟机将常量池中符号引用转化为直接引用的过程**

5. **初始化：类加载的最后一步。这阶段代码才真正执行。准备阶段赋过值，在这阶段可以根据需求赋值，执行类构造器 <Clinit>()方法**

   

### 16.类初始化的时机

1. new时
2. 访问某类或接口的静态变量
3. 调用类的静态方法
4. 反射
5. 初始化子类，父类也会初始化
6. JVM启动时被标明为启动类的类



# MySQL

### 1.MyISAM和InnoDB区别

MyISAM是MySQL的默认数据库引擎（5.5版之前）。虽然性能极佳，而且提供了大量的特性，包括全文索引、压缩、空间函数等，但MyISAM不支持事务和行级锁，而且最大的缺陷就是崩溃后无法安全恢复。不过，5.5版本之后，MySQL引入了InnoDB（事务性数据库引擎），MySQL 5.5版本后默认的存储引擎为InnoDB。

1. **是否支持行级锁** : MyISAM 只有表级锁(table-level locking)，而InnoDB 支持行级锁(row-level locking)和表级锁,默认为行级锁。

2. **是否支持事务和崩溃后的安全恢复： MyISAM** 强调的是性能，每次查询具有原子性,其执行速度比InnoDB类型更快，但是不提供事务支持。但是**InnoDB** 提供事务支持事务，外部键等高级数据库功能。 具有事务(commit)、回滚(rollback)和崩溃修复能力(crash recovery capabilities)的事务安全(transaction-safe (ACID compliant))型表。

3. **是否支持外键：** MyISAM不支持，而InnoDB支持。

4. **是否支持MVCC** ：仅 InnoDB 支持。应对高并发事务, MVCC比单纯的加锁更高效;MVCC只在 `READ COMMITTED` 和 `REPEATABLE READ` 两个隔离级别下工作;MVCC可以使用 乐观(optimistic)锁 和 悲观(pessimistic)锁来实现;各数据库中MVCC实现并不统一。

   

### 2.索引底层结构

MySQL索引使用的数据结构主要有**BTree索引** 和 **哈希索引** 。对于哈希索引来说，底层的数据结构就是哈希表，因此在绝大多数需求为单条记录查询的时候，可以选择哈希索引，查询性能最快；其余大部分场景，建议选择BTree索引。

MySQL的BTree索引使用的是B树中的B+Tree，但对于主要的两种存储引擎的实现方式是不同的。

- **MyISAM:** B+Tree叶节点的data域存放的是数据记录的地址。在索引检索的时候，首先按照B+Tree搜索算法搜索索引，如果指定的Key存在，则取出其 data 域的值，然后以 data 域的值为地址读取相应的数据记录。这被称为“非聚簇索引”。
- **InnoDB:** 其数据文件本身就是索引文件。相比MyISAM，索引文件和数据文件是分离的，其表数据文件本身就是按B+Tree组织的一个索引结构，树的叶节点data域保存了完整的数据记录。这个索引的key是数据表的主键，因此InnoDB表数据文件本身就是主索引。这被称为“聚簇索引（或聚集索引）”。而其余的索引都作为辅助索引，辅助索引的data域存储相应记录主键的值而不是地址，这也是和MyISAM不同的地方。**在根据主索引搜索时，直接找到key所在的节点即可取出数据；在根据辅助索引查找时，则需要先取出主键的值，再走一遍主索引。** **因此，在设计表的时候，不建议使用过长的字段作为主键，也不建议使用非单调的字段作为主键，这样会造成主索引频繁分裂。** 

### 3.B+树

**B+树数据结构**：

1. 根节点至少两个子女
2. 每个中间节点至少包含ceil(m/2)个孩子，至多m个孩子
3. 每个叶子节点包含k-1个元素，其中m/2 <= k <= m
4. 所有叶子节点在同一层
5. 每个节点中元素从小到大排列

**B+树：**

1. 有k个子节点的节点有k个关键码
2. 非叶节点只有索引作用，和记录有关的信息都在叶节点
3. 树的所有叶节点构成有序链表，可按照关键码排序次序遍历

B+树中间节点不存储数据，同样大小的磁盘页可以容纳更多节点元素，IO查询次数更少。

**B+树和B树相比：**

1. IO次数更少
2. 查询性能更稳定，因为所有关键字查询路径长度相同
3. 范围查询更简便

**使用B+树而不是B树的原因：**

**B+树只要遍历叶子节点就能遍历整棵树，能基于范围查询；B树只能中序遍历，效率低。**



### **4.聚集索引和非聚集索引**

索引分类：Mysql中有**聚集索引（主键索引）和非聚集索引（普通索引、唯一索引、全文索引）**

1. **聚集（clustered）索引，也叫聚簇索引：**

   **数据行的物理顺序与列值（一般是主键的那一列）的逻辑顺序相同，一个表中只能拥有一个聚集索引。**

   优点：查找快，因为连续；

   缺点：修改慢，因为要保持表中记录和索引顺序一致

   ![聚集索引](/Users/zhangyi/Desktop/Java复习图/聚集索引.png)

   聚集索引的好处了，**索引的叶子节点就是对应的数据节点**（MySQL的MyISAM除外，此存储引擎的聚集索引和非聚集索引只多了个唯一约束，其他没什么区别），可以直接获取到对应的全部列的数据，而非聚集索引在索引没有覆盖到对应的列的时候需要进行二次查询。

   **InnoDB中，如果定义主键，主键则为聚集索引；**

   **如果不定义，第一个Not Null Unique为聚集索引**

   **否则InnoDB会自动创建一个隐含列作为表的聚集索引。**

   创建方式：

   1. 创建表的时候指定主键（MySQL里主键就是聚集索引）

      ```sql
      create table t1(
      	id int primary key,
      	name nvarchar(255)
      )
      ```

   2. 创建表后添加聚集索引

      ```sql
      alter table table_name add primary key(colum_name)
      ```

      值得注意的是，**最好还是在创建表的时候添加聚集索引**，由于聚集索引的物理顺序上的特殊性，因此**如果再在上面创建索引的时候会根据索引列的排序移动全部数据行上面的顺序，会非常地耗费时间以及性能**。

2. **非聚集（unclustered）索引：**

   **索引中索引的逻辑顺序与磁盘上行的物理存储顺序不同，一个表中可以拥有多个非聚集索引。**

   **除了聚集索引以外的索引都是非聚集索引，只是人们想细分一下非聚集索引，分成普通索引，唯一索引，全文索引。**

   ![非聚集索引](/Users/zhangyi/Desktop/Java复习图/非聚集索引.png)

   **非聚集索引二次查询：**非聚集索引叶节点仍然是索引节点**，只是**有一个指针指向对应的数据块**，此时如果使用非聚集索引查询，而**查询列中包含了其他该索引没有覆盖的列，那么他还要进行第二次的查询，查询节点上对应的数据行的数据。

   MySQL里面就算表里数据量少且查询了非键列，也不会使用聚集索引去全索引扫描，而是直接二次查询。

   **同时非聚集索引其实叶子节点除了会存储索引覆盖列的数据，也会存放聚集索引所覆盖的列数据。**

   **如何解决非聚集索引的二次查询问题：**

   **建立覆盖索引（联合索引）：建立两列以上的索引，即可查询复合索引里的列的数据而不需要进行回表二次查询，如index(col1, col2)**

   ```sql
   select col1, col2 from t1 where col1 = '213';
   ```

   **注意：使用复合索引需要满足最左侧索引的原则，也就是查询的时候如果where条件里面没有最左边的一到多列，索引就不会起作用。**

### 5.索引的优点：

1. 加快数据检索速度
2. 加速表之间连接
3. 创建唯一索引，保证表中每一行数据唯一性

### 6.事务

1. **事务四大特性（ACID）**

   1. **原子性（Atomicity）：** 事务是最小的执行单位，不允许分割。事务的原子性确保动作要么全部完成，要么完全不起作用；
   2. **一致性（Consistency）：** 执行事务前后，数据保持一致，多个事务对同一个数据读取的结果是相同的；
   3. **隔离性（Isolation）：** 并发访问数据库时，一个用户的事务不被其他事务所干扰，各并发事务之间数据库是独立的；
   4. **持久性（Durability）：** 一个事务被提交之后。它对数据库中数据的改变是持久的，即使数据库发生故障也不应该对其有任何影响。

2. **并发事务有哪些问题**

   - **脏读（Dirty read）:** 当一个事务正在访问数据并且对数据进行了修改，而这种修改还没有提交到数据库中，这时另外一个事务也访问了这个数据，然后使用了这个数据。因为这个数据是还没有提交的数据，那么另外一个事务读到的这个数据是“脏数据”，依据“脏数据”所做的操作可能是不正确的。
   - **丢失修改（Lost to modify）:** 指在一个事务读取一个数据时，另外一个事务也访问了该数据，那么在第一个事务中修改了这个数据后，第二个事务也修改了这个数据。这样第一个事务内的修改结果就被丢失，因此称为丢失修改。 例如：事务1读取某表中的数据A=20，事务2也读取A=20，事务1修改A=A-1，事务2也修改A=A-1，最终结果A=19，事务1的修改被丢失。
   - **不可重复读（Unrepeatableread）:** 指在一个事务内多次读同一数据。在这个事务还没有结束时，另一个事务也访问该数据。那么，在第一个事务中的两次读数据之间，由于第二个事务的修改导致第一个事务两次读取的数据可能不太一样。这就发生了在一个事务内两次读到的数据是不一样的情况，因此称为不可重复读。
   - **幻读（Phantom read）:** 幻读与不可重复读类似。它发生在一个事务（T1）读取了几行数据，接着另一个并发事务（T2）插入了一些数据时。在随后的查询中，第一个事务（T1）就会发现多了一些原本不存在的记录，就好像发生了幻觉一样，所以称为幻读。

   **不可重复读和幻读区别：**

   不可重复读的重点是修改比如多次读取一条记录发现其中某些列的值被修改，幻读的重点在于新增或者删除比如多次读取一条记录发现记录增多或减少了。

3. **事务隔离级别**

   - **READ-UNCOMMITTED(读取未提交)：** 最低的隔离级别，允许读取尚未提交的数据变更，**可能会导致脏读、幻读或不可重复读**。
   - **READ-COMMITTED(读取已提交)：** 允许读取并发事务已经提交的数据，**可以阻止脏读，但是幻读或不可重复读仍有可能发生**。
   - **REPEATABLE-READ(可重复读)：** 对同一字段的多次读取结果都是一致的，除非数据是被本身事务自己所修改，**可以阻止脏读和不可重复读，但幻读仍有可能发生**。
   - **SERIALIZABLE(可串行化)：** 最高的隔离级别，完全服从ACID的隔离级别。所有的事务依次逐个执行，这样事务之间就完全不可能产生干扰，也就是说，**该级别可以防止脏读、不可重复读以及幻读**。

   MySQL InnoDB 存储引擎的默认支持的隔离级别是 **REPEATABLE-READ（可重读）**。我们可以通过`SELECT @@tx_isolation;`命令来查看

   与 SQL 标准不同的地方在于 InnoDB 存储引擎在 **REPEATABLE-READ（可重读）** 事务隔离级别下使用的是Next-Key Lock 锁算法，因此可以避免幻读的产生，这与其他数据库系统(如 SQL Server) 是不同的。

   因为隔离级别越低，事务请求的锁越少，所以大部分数据库系统的隔离级别都是 **READ-COMMITTED(读取提交内容)** ，但是InnoDB 存储引擎默认使用 **REPEAaTABLE-READ（可重读）** 并不会有任何性能损失。

   

### 7.锁机制

1. **MyISAM和InnoDB存储引擎使用的锁：**

   - MyISAM采用表级锁(table-level locking)。
   - InnoDB支持行级锁(row-level locking)和表级锁,默认为行级锁

2. **表级锁和行级锁对比：**

   - **表级锁：** MySQL中锁定 **粒度最大** 的一种锁，对当前操作的整张表加锁，实现简单，资源消耗也比较少，加锁快，不会出现死锁。其锁定粒度最大，触发锁冲突的概率最高，并发度最低，MyISAM和 InnoDB引擎都支持表级锁。
   - **行级锁：** MySQL中锁定 **粒度最小** 的一种锁，只针对当前操作的行进行加锁。 行级锁能大大减少数据库操作的冲突。其加锁粒度最小，并发度高，但加锁的开销也最大，加锁慢，会出现死锁。

3. **InnoDB存储引擎的锁的算法有三种：**

   - **Record lock：单个行记录上的锁**
   - **Gap lock：间隙锁，锁定一个范围，不包括记录本身**
   - **Next-key lock：record+gap 锁定一个范围，包含记录本身**

4. **锁分类：表级锁和行级锁可以进一步划分为共享锁（s）和排他锁（X）。**

   - 共享锁（s）

     **共享锁（Share Locks，简记为S）又被称为读锁**，其他用户可以并发读取数据，但任何事务都不能获取数据上的排他锁，直到已释放所有共享锁。

     共享锁(S锁)又称为读锁，若事务T对数据对象A加上S锁，则事务T只能读A；其他事务只能再对A加S锁，而不能加X锁，直到T释放A上的S锁。这就保证了其他事务可以读A，但在T释放A上的S锁之前不能对A做任何修改。

   - 排他锁（X）：

     **排它锁（(Exclusive lock,简记为X锁)）又称为写锁**，若事务T对数据对象A加上X锁，则只允许T读取和修改A，其它任何事务都不能再对A加任何类型的锁，直到T释放A上的锁。它防止任何其它事务获取资源上的锁，直到在事务的末尾将资源上的原始锁释放为止。在更新操作(INSERT、UPDATE 或 DELETE)过程中始终应用排它锁。

   **两者区别：**

   1. 共享锁（S锁）：如果事务T对数据A加上共享锁后，则其他事务只能对A再加共享锁，不 能加排他锁。获取共享锁的事务只能读数据，不能修改数据。
   2. 排他锁（X锁）：如果事务T对数据A加上排他锁后，则其他事务不能再对A加任任何类型的封锁。获取排他锁的事务既能读数据，又能修改数据。

5. **另外两个表级锁：IS和IX**

   当一个事务需要给自己需要的某个资源加锁的时候，如果遇到一个共享锁正锁定着自己需要的资源的时候，自己可以再加一个共享锁，不过不能加排他锁。但是，如果遇到自己需要锁定的资源已经被一个排他锁占有之后，则只能等待该锁定释放资源之后自己才能获取锁定资源并添加自己的锁定。

   **意向锁的作用就是当一个事务在需要获取资源锁定的时候，如果遇到自己需要的资源已经被排他锁占用的时候，该事务可以需要锁定行的表上面添加一个合适的意向锁。**如果自己需要一个共享锁，那么就在表上面添加一个意向共享锁。而如果自己需要的是某行（或者某些行）上面添加一个排他锁的话，则先在表上面添加一个意向排他锁。意向共享锁可以同时并存多个，但是意向排他锁同时只能有一个存在。

   **InnoDB另外的两个表级锁：**

   - **意向共享锁（IS）：** 表示事务准备给数据行记入共享锁，事务在一个数据行加共享锁前必须先取得该表的IS锁。
   - **意向排他锁（IX）：** 表示事务准备给数据行加入排他锁，事务在一个数据行加排他锁前必须先取得该表的IX锁。

   **注意：**

   1. **这里的意向锁是表级锁，表示的是一种意向，仅仅表示事务正在读或写某一行记录，在真正加行锁时才会判断是否冲突。意向锁是InnoDB自动加的，不需要用户干预。**
   2. **IX，IS是表级锁，不会和行级的X，S锁发生冲突，只会和表级的X，S发生冲突。**

6. **死锁和避免死锁**

   **InnoDB的行级锁是基于索引实现的，如果查询语句为命中任何索引，那么InnoDB会使用表级锁.** 此外，InnoDB的行级锁是针对索引加的锁，不针对数据记录，因此即使访问不同行的记录，如果使用了相同的索引键仍然会出现锁冲突

   通过**`SELECT ...LOCK IN SHARE MODE;`和`SELECT ...FOR UPDATE;`**使用锁的时候，如果表没有定义任何索引，那么InnoDB会创建一个隐藏的聚簇索引并使用这个索引来加记录锁。

   此外，**不同于MyISAM总是一次性获得所需的全部锁，InnoDB的锁是逐步获得的，当两个事务都需要获得对方持有的锁，导致双方都在等待，这就产生了死锁。** 发生死锁后，InnoDB一般都可以检测到，并使一个事务释放锁回退，另一个则可以获取锁完成事务，我们可以采取以上方式避免死锁：

   - **通过表级锁来减少死锁产生的概率；**
   - **多个程序尽量约定以相同的顺序访问表（这也是解决并发理论中哲学家就餐问题的一种思路）；**
   - **同一个事务尽可能做到一次锁定所需要的所有资源。**

   **补充：**

   **页级锁：** MySQL中锁定粒度介于行级锁和表级锁中间的一种锁。表级锁速度快，但冲突多，行级冲突少，但速度慢。页级进行了折衷，一次锁定相邻的一组记录。BDB支持页级锁。开销和加锁时间界于表锁和行锁之间，会出现死锁。锁定粒度界于表锁和行锁之间，并发度一般。

### 8.MVCC - 多版本并发控制

#### MVCC基本概念

1. **什么是MVCC？**

   **`MVCC`**，全称 `Multi-Version Concurrency Control` ，即多版本并发控制。MVCC 是一种并发控制的方法，一般在数据库管理系统中，实现对数据库的并发访问，在编程语言中实现事务内存。

   **MVCC** 在 **MySQL InnoDB** 中的实现主要是为了提高数据库并发性能，用更好的方式去处理读-写冲突，做到即使有读写冲突时，也能做到不加锁，非阻塞并发读

2. 什么是当前读和快照读？

   - 当前读
     像 **select xxx lock in share mode (共享锁),** **select for update; update; insert; delete (排他锁**)这些操作都是一种当前读，为什么叫当前读？就是它读取的是记录的最新版本，读取时还要保证其他并发事务不能修改当前记录，会对读取的记录进行加锁
   - 快照读
     **像不加锁的 select 操作就是快照读，即不加锁的非阻塞读**；快照读的前提是隔离级别不是串行级别，串行级别下的快照读会退化成当前读；之所以出现快照读的情况，是基于提高并发性能的考虑，快照读的实现是基于多版本并发控制，即 MVCC ,可以认为 MVCC 是行锁的一个变种，但它在很多情况下，避免了加锁操作，降低了开销；既然是基于多版本，即**快照读可能读到的并不一定是数据的最新版本，而有可能是之前的历史版本**

   **MVCC其实就是为了实现读-写冲突不加锁，而这个读指的就是`快照读`, 而非当前读，当前读实际上是一种加锁的操作，是悲观锁的实现**

3. **当前读、快照读和MVCC的关系**

   - MVCC 多版本并发控制是 **「维持一个数据的多个版本，使得读写操作没有冲突」** 的概念，只是一个抽象概念，并非实现
   - 因为 MVCC 只是一个抽象概念，要实现这么一个概念，MySQL 就需要提供具体的功能去实现它，**「快照读就是 MySQL 实现 MVCC 理想模型的其中一个非阻塞读功能」**。而相对而言，当前读就是悲观锁的具体功能实现
   - 要说的再细致一些，快照读本身也是一个抽象概念，再深入研究。MVCC 模型在 MySQL 中的具体实现则是由 **`3 个隐式字段`,`undo log`, `Read View`** 等去完成的

4. **MVCC解决的问题以及好处**

   **数据库并发场景有三种，分别为：**

   - `读-读`：不存在任何问题，也不需要并发控制
   - `读-写`：有线程安全问题，可能会造成事务隔离性问题，可能遇到脏读，幻读，不可重复读
   - `写-写`：有线程安全问题，可能会存在更新丢失问题，比如第一类更新丢失，第二类更新丢失

   **MVCC 带来的好处：**
   多版本并发控制（MVCC）是一种**用来解决读-写冲突的无锁并发控制**，也就是为事务分配单向增长的时间戳，为每个修改保存一个版本，版本与事务时间戳关联，读操作只读该事务开始前的数据库的快照。 所以 MVCC 可以为数据库解决以下问题：

   - 在并发读写数据库时，可以做到在读操作时不用阻塞写操作，写操作也不用阻塞读操作，提高了数据库并发读写的性能
   - 同时还可以解决脏读，幻读，不可重复读等事务隔离问题，但不能解决更新丢失问题

5. 小结
   简而言之，MVCC 就是因为不满意只让数据库采用悲观锁这样性能不佳的形式去解决读-写冲突问题，而提出的解决方案，所以**在数据库中，因为有了 MVCC，所以我们可以形成两个组合：**

   - **MVCC + 悲观锁**
     MVCC解决读写冲突，悲观锁解决写写冲突
   - **MVCC + 乐观锁**
     MVCC 解决读写冲突，乐观锁解决写写冲突

   这种组合的方式就可以最大程度的提高数据库并发性能，并解决读写冲突，和写写冲突导致的问题

#### MVCC实现原理

MVCC 的目的就是多版本并发控制，在数据库中的实现，就是为了解决`读写冲突`，它的实现原理主要是依赖记录中的 **`3个隐式字段`**，**`undo log`** ，**`Read View`** 来实现的。

1. **`隐式字段`**

   每行记录除了我们自定义的字段外，还有数据库隐式定义的 `DB_TRX_ID`, `DB_ROLL_PTR`, `DB_ROW_ID` 等字段

   - `DB_TRX_ID`
     6 byte，最近修改(`修改/插入`)事务 ID：记录创建这条记录/最后一次修改该记录的事务 ID
   - `DB_ROLL_PTR`
     7 byte，回滚指针，指向这条记录的上一个版本（存储于 rollback segment 里）
   - `DB_ROW_ID`
     6 byte，隐含的自增 ID（隐藏主键），如果数据表没有主键，InnoDB 会自动以`DB_ROW_ID`产生一个聚簇索引
   - 实际还有一个删除 flag 隐藏字段, 即记录被更新或删除并不代表真的删除，而是删除 flag 变了

   ![InnoDB隐式字段](/Users/zhangyi/Desktop/Java复习图/InnoDB隐式字段.png)

   如上图，`DB_ROW_ID` 是数据库默认为该行记录生成的唯一隐式主键，`DB_TRX_ID` 是当前操作该记录的事务 ID ,而 `DB_ROLL_PTR` 是一个回滚指针，用于配合 undo日志，指向上一个旧版本

   

2. **`undo log`**

   undo log 主要分为两种：

   - **insert undo log**
     代表事务在 `insert` 新记录时产生的 `undo log`, 只在事务回滚时需要，并且在事务提交后可以被立即丢弃
   - **update undo log**
     事务在进行 `update` 或 `delete` 时产生的 `undo log` ; 不仅在事务回滚时需要，在快照读时也需要；所以不能随便删除，只有在快速读或事务回滚不涉及该日志时，对应的日志才会被 `purge` 线程统一清除

   **purge线程**

   - 从前面的分析可以看出，为了实现 InnoDB 的 MVCC 机制，更新或者删除操作都只是设置一下老记录的 deleted_bit ，并不真正将过时的记录删除。
   - 为了节省磁盘空间，InnoDB 有专门的 purge 线程来清理 deleted_bit 为 true 的记录。为了不影响 MVCC 的正常工作，purge 线程自己也维护了一个read view（这个 read view 相当于系统中最老活跃事务的 read view ）;如果某个记录的 deleted_bit 为 true ，并且 DB_TRX_ID 相对于 purge 线程的 read view 可见，那么这条记录一定是可以被安全清除的。

   对 MVCC 有帮助的实质是 `update undo log` ，`undo log` 实际上就是存在 `rollback segment` 中旧记录链，**它的执行流程如下：**

   1. **比如一个有个事务插入 persion 表插入了一条新记录，记录如下，`name` 为 Yi, `age` 为 26 岁，`隐式主键`是 1，`事务 ID`和`回滚指针`，我们假设为 NULL**

      ![undolog1](/Users/zhangyi/Desktop/Java复习图/undolog1.png)

   2. **现在来了一个`事务 1`对该记录的 `name` 做出了修改，改为 Tom**

      - 在`事务 1`修改该行(记录)数据时，数据库会先对该行加`排他锁`

      - 然后把该行数据拷贝到 `undo log` 中，作为旧记录，即在 `undo log` 中有当前行的拷贝副本

      - 拷贝完毕后，修改该行`name`为Tom，并且修改隐藏字段的事务 ID 为当前`事务 1`的 ID, 我们默认从 `1` 开始，之后递增，回滚指针指向拷贝到 `undo log` 的副本记录，既表示我的上一个版本就是它

      - 事务提交后，释放锁

        ![undolog2](/Users/zhangyi/Desktop/Java复习图/undolog2.png)

   3. **又来了个`事务 2`修改`person 表`的同一个记录，将`age`修改为 30 岁**

      - 在`事务2`修改该行数据时，数据库也先为该行加锁

      - 然后把该行数据拷贝到 `undo log` 中，作为旧记录，发现该行记录已经有 `undo log` 了，那么最新的旧数据作为链表的表头，插在该行记录的 `undo log` 最前面

      - 修改该行 `age` 为 30 岁，并且修改隐藏字段的事务 ID 为当前`事务 2`的 ID, 那就是 `2` ，回滚指针指向刚刚拷贝到 `undo log` 的副本记录

      - 事务提交，释放锁

        ![undolog3](/Users/zhangyi/Desktop/Java复习图/undolog3.png)

        从上面可以看出，**不同事务或者相同事务的对同一记录的修改，会导致该记录的undo log成为一条记录版本线性表，既链表，undo log 的链首就是最新的旧记录，链尾就是最早的旧记录**（该 undo log 的节点可能是会被 purge 线程清除掉，像图中的第一条 insert undo log，其实在事务提交之后可能就被删除丢失了，不过这里为了演示，所以还放在这里）
        

3. **`Read view 读视图`**

   1. **什么是Read View：**

      - Read View 就是事务进行**`快照读`**操作的时候生产的**`读视图` (Read View)**，在该事务执行的快照读的那一刻，会生成数据库系统当前的一个快照，记录并维护系统当前活跃事务的 ID (**当每个事务开启时，都会被分配一个 ID , 这个 ID 是递增的，所以最新的事务，ID 值越大**)。
      - 所以我们知道 `Read View` 主要是用来做可见性判断的, 即当我们某个事务执行快照读的时候，对该记录创建一个 `Read View` 读视图，把它比作条件用来判断当前事务能够看到哪个版本的数据，既可能是当前最新的数据，也有可能是该行记录的`undo log`里面的某个版本的数据。
      - `Read View`遵循一个可见性算法，主要是将**`要被修改的数据`**的最新记录中的 **`DB_TRX_ID`**（即**当前事务 ID** ）取出来，与系统当前其他活跃事务的 ID 去对比（由 Read View 维护），如果 **`DB_TRX_ID`** 跟Read View 的属性做了某些比较，不符合可见性，那就通过 **`DB_ROLL_PTR`** 回滚指针去取出 **`Undo Log`** 中的 **`DB_TRX_ID`** 再比较，即**遍历链表的 `DB_TRX_ID`**（从链首到链尾，即从最近的一次修改查起），直到找到满足特定条件的 **`DB_TRX_ID`** , **那么这个 DB_TRX_ID 所在的旧记录就是当前事务能看见的最新`老版本`**

   2. **DB_TRX_ID 如何跟 Read View 某些属性进行怎么样的比较**：

      先简化一下 Read View，我们可以把 Read View 简单的理解成有三个全局属性：

      - ```
        trx_list（名称我随意取的）
        ```

        - 一个数值列表
        - 用于维护 Read View 生成时刻系统 **正活跃的事务 ID 列表**

      - ```
        up_limit_id
        ```

        - lower water remark
        - **是 trx_list 列表中事务 ID 最小的 ID**

      - ```
        low_limit_id
        ```

        - hight water mark
        - ReadView 生成时刻系统尚未分配的下一个事务 ID ，也就是 **目前已出现过的事务 ID 的最大值 + 1**
        - 为什么是 low_limit ? 因为它也是系统此刻可分配的事务 ID 的最小值

      比较流程：

      - 首先比较 `DB_TRX_ID < up_limit_id` , 如果小于，则当前事务能看到 `DB_TRX_ID` 所在的记录，如果大于等于进入下一个判断
      - 接下来判断 `DB_TRX_ID >= low_limit_id` , 如果大于等于则代表 `DB_TRX_ID` 所在的记录在 `Read View` 生成后才出现的，那对当前事务肯定不可见，如果小于则进入下一个判断
      - 判断 `DB_TRX_ID` 是否在活跃事务之中，`trx_list.contains (DB_TRX_ID)`，如果在，则代表我 `Read View` 生成时刻，你这个事务还在活跃，还没有 Commit，你修改的数据，我当前事务也是看不见的；如果不在，则说明，你这个事务在 `Read View` 生成之前就已经 Commit 了，你修改的结果，我当前事务是能看见的

4. **整体MVCC流程**

   1. 当`事务 2`对某行数据执行了`快照读`，数据库为该行数据生成一个`Read View`读视图，假设当前事务 ID 为 `2`，此时还有`事务1`和`事务3`在活跃中，`事务 4`在`事务 2`快照读前一刻提交更新了，所以 Read View 记录了系统当前活跃事务 1，3 的 ID，维护在一个列表上，假设我们称为`trx_list`

      |          |          |          |              |
      | -------- | -------- | -------- | ------------ |
      | 事务 1   | 事务 2   | 事务 3   | 事务 4       |
      | 事务开始 | 事务开始 | 事务开始 | 事务开始     |
      | …        | …        | …        | 修改且已提交 |
      | 进行中   | 快照读   | 进行中   |              |
      | …        | …        | …        |              |

   2. Read View 不仅仅会通过一个列表 `trx_list` 来维护`事务 2`执行`快照读`那刻系统正活跃的事务 ID 列表，还会有两个属性 `up_limit_id`（ **trx_list 列表中事务 ID 最小的 ID** ），`low_limit_id` ( **快照读时刻系统尚未分配的下一个事务 ID ，也就是目前已出现过的事务ID的最大值 + 1**。所以在这里例子中 `up_limit_id` 就是1，`low_limit_id` 就是 4 + 1 = 5，trx_list 集合的值是 1, 3，`Read View` 如下图

      ![MVCC流程1](/Users/zhangyi/Desktop/Java复习图/MVCC流程1.png)

   3. 我们的例子中，只有`事务 4` 修改过该行记录，并在`事务 2` 执行`快照读`前，就提交了事务，所以当前该行当前数据的 `undo log` 如下图所示；我们的事务 2 在快照读该行记录的时候，就会拿该行记录的 `DB_TRX_ID` 去跟 `up_limit_id` , `low_limit_id` 和`活跃事务 ID 列表( trx_list )`进行比较，判断当前`事务 2`能看到该记录的版本是哪个。

      ![MVCC流程2](/Users/zhangyi/Desktop/Java复习图/MVCC流程2.png)

   4. 所以先拿该记录 `DB_TRX_ID` 字段记录的事务 ID `4` 去跟 `Read View` 的 `up_limit_id` 比较，看 `4` 是否小于 `up_limit_id`( 1 )，所以不符合条件，继续判断 `4` 是否大于等于 `low_limit_id`(5 )，也不符合条件，最后判断 `4` 是否处于 `trx_list` 中的活跃事务, 最后发现事务 ID 为 `4` 的事务不在当前活跃事务列表中, 符合可见性条件，所以`事务 4`修改后提交的最新结果对`事务 2` 快照读时是可见的，所以`事务 2` 能读到的最新数据记录是`事务4`所提交的版本，而事务4提交的版本也是全局角度上最新的版本

      ![MVCC流程3](/Users/zhangyi/Desktop/Java复习图/MVCC流程3.png)

   5. 也正是 Read View 生成时机的不同，从而造成 RC , RR 级别下快照读的结果的不同

#### MVCC相关问题

1. 当前读和快照读在RR级别下的区别：

   | 事务A                       | 事务B                                      |
   | --------------------------- | ------------------------------------------ |
   | 开启事务                    | 开启事务                                   |
   | 快照读(无影响)查询金额为500 | 快照读查询金额为500                        |
   | 更新金额为400               |                                            |
   | 提交事务                    |                                            |
   |                             | select `快照读`金额为500                   |
   |                             | select lock in share mode`当前读`金额为400 |

   **在上表的顺序下，事务 B 的在事务 A 提交修改后的快照读是旧版本数据，而当前读是实时新数据 400**

   表二：

   | 事务A                         | 事务B                                      |
   | ----------------------------- | ------------------------------------------ |
   | 开启事务                      | 开启事务                                   |
   | 快照读（无影响）查询金额为500 |                                            |
   | 更新金额为400                 |                                            |
   | 提交事务                      |                                            |
   |                               | select `快照读`金额为400                   |
   |                               | select lock in share mode`当前读`金额为400 |

   而在`表 2`这里的顺序中，事务 B 在事务 A 提交后的快照读和当前读都是实时的新数据 400，这是为什么呢？

   - 这里与上表的唯一区别仅仅是`表 1`的事务 B 在事务 A 修改金额前`快照读`过一次金额数据，而`表 2`的事务B在事务A修改金额前没有进行过快照读。

   **所以事务中快照读的结果是非常依赖该事务首次出现快照读的地方，即某个事务中首次出现快照读的地方非常关键，它有决定该事务后续快照读结果的能力**

   **我们这里测试的是`更新`，同时`删除`和`更新`也是一样的，如果事务B的快照读是在事务A操作之后进行的，事务B的快照读也是能读取到最新的数据的**

2. **RC(Read Committed)和RR(Repeatable Read)级别下快照读有何不同**

   - 在 RR 级别下的某个事务的对某条记录的第一次快照读会创建一个快照及 Read View, 将当前系统活跃的其他事务记录起来，此后在调用快照读的时候，还是使用的是同一个 Read View，所以只要当前事务在其他事务提交更新之前使用过快照读，那么之后的快照读使用的都是同一个 Read View，所以对之后的修改不可见；
   - 即 RR 级别下，快照读生成 Read View 时，Read View 会记录此时所有其他活动事务的快照，这些事务的修改对于当前事务都是不可见的。而早于Read View创建的事务所做的修改均是可见
   - 而在 RC 级别下的，事务中，每次快照读都会新生成一个快照和 Read View , 这就是我们在 RC 级别下的事务中可以看到别的事务提交的更新的原因

   **总之在 RC 隔离级别下，是每个快照读都会生成并获取最新的 Read View；而在 RR 隔离级别下，则是同一个事务中的第一个快照读才会创建 Read View, 之后的快照读获取的都是同一个 Read View。**

### 9.一条SQL语句在MySQL如何执行

1. **MySQL基本架构概览**

   - **连接器：** 身份认证和权限相关(登录 MySQL 的时候)。
   - **查询缓存:** 执行查询语句的时候，会先查询缓存（MySQL 8.0 版本后移除，因为这个功能不太实用）。
   - **分析器:** 没有命中缓存的话，SQL 语句就会经过分析器，分析器说白了就是要先看你的 SQL 语句要干嘛，再检查你的 SQL 语句语法是否正确。
   - **优化器：** 按照 MySQL 认为最优的方案去执行。
   - **执行器:** 执行语句，然后从存储引擎返回数据。

   ![Mysql架构](/Users/zhangyi/Desktop/Java复习图/Mysql架构.png)

   简单来说 MySQL 主要分为 Server 层和存储引擎层：

   - **Server 层**：主要包括连接器、查询缓存、分析器、优化器、执行器等，所有跨存储引擎的功能都在这一层实现，比如存储过程、触发器、视图，函数等，还有一个通用的日志模块 binglog 日志模块。
   - **存储引擎**： 主要负责数据的存储和读取，采用可以替换的插件式架构，支持 InnoDB、MyISAM、Memory 等多个存储引擎，其中 InnoDB 引擎有自有的日志模块 redolog 模块。**现在最常用的存储引擎是 InnoDB，它从 MySQL 5.5.5 版本开始就被当做默认存储引擎了。**

2. **Server层基本组件介绍**

   1. **连接器**

      **连接器主要和身份认证和权限相关的功能相关，就好比一个级别很高的门卫一样。**

      主要负责用户登录数据库，进行用户的身份认证，包括校验账户密码，权限等操作，如果用户账户密码已通过，连接器会到权限表中查询该用户的所有权限，之后在这个连接里的权限逻辑判断都是会依赖此时读取到的权限数据，也就是说，后续只要这个连接不断开，即时管理员修改了该用户的权限，该用户也是不受影响的。

   2. **查询缓存**

      **查询缓存主要用来缓存我们所执行的 SELECT 语句以及该语句的结果集。**

      连接建立后，执行查询语句的时候，会先查询缓存，MySQL 会先校验这个 sql 是否执行过，以 Key-Value 的形式缓存在内存中，Key 是查询预计，Value 是结果集。如果缓存 key 被命中，就会直接返回给客户端，如果没有命中，就会执行后续的操作，完成后也会把结果缓存起来，方便下一次调用。当然在真正执行缓存查询的时候还是会校验用户的权限，是否有该表的查询条件。

      MySQL 8.0 版本后删除了缓存的功能，官方也是认为该功能在实际的应用场景比较少，所以干脆直接删掉了。

   3. **分析器**

      MySQL 没有命中缓存，那么就会进入分析器，分析器主要是用来分析 SQL 语句是来干嘛的，分析器也会分为几步：

      - **第一步，词法分析**，一条 SQL 语句有多个字符串组成，首先要提取关键字，比如 select，提出查询的表，提出字段名，提出查询条件等等。做完这些操作后，就会进入第二步。

      - **第二步，语法分析**，主要就是判断你输入的 sql 是否正确，是否符合 MySQL 的语法。

   4. **优化器**

      优化器的作用就是它认为的最优的执行方案去执行（有时候可能也不是最优，这篇文章涉及对这部分知识的深入讲解），比如多个索引的时候该如何选择索引，多表查询的时候如何选择关联顺序等。

      可以说，经过了优化器之后可以说这个语句具体该如何执行就已经定下来。

   5. **执行器**

      当选择了执行方案后，MySQL 就准备开始执行了，首先执行前会校验该用户有没有权限，如果没有权限，就会返回错误信息，如果有权限，就会去调用引擎的接口，返回接口执行的结果。

3. **查询语句分析**

   分析以下查询语句：

   ```sql
   select * from tb_student A where A.age='18' and A.name='张三';
   ```

   - 先检查该语句是否有权限，如果没有权限，直接返回错误信息，如果有权限，在 MySQL8.0 版本以前，会先查询缓存，以这条 sql 语句为 key 在内存中查询是否有结果，如果有直接缓存，如果没有，执行下一步。

   - 通过分析器进行词法分析，提取 sql 语句的关键元素，比如提取上面这个语句是查询 select，提取需要查询的表名为 tb_student,需要查询所有的列，查询条件是这个表的 id='1'。然后判断这个 sql 语句是否有语法错误，比如关键词是否正确等等，如果检查没问题就执行下一步。

   - 接下来就是优化器进行确定执行方案，上面的 sql 语句，可以有两种执行方案：

     ```
      a.先查询学生表中姓名为“张三”的学生，然后判断是否年龄是 18。  
      b.先找出学生中年龄 18 岁的学生，然后再查询姓名为“张三”的学生。
     ```

     那么优化器根据自己的优化算法进行选择执行效率最好的一个方案（优化器认为，有时候不一定最好）。那么确认了执行计划后就准备开始执行了。

   - 进行权限校验，如果没有权限就会返回错误信息，如果有权限就会调用数据库引擎接口，返回引擎的执行结果。

4. **更新语句分析**

   一条更新语句如何执行的呢？sql 语句如下：

   ```sql
   update tb_student A set A.age='19' where A.name=' 张三 ';
   ```

   更新的时候要记录日志啦，这就会引入日志模块了。

   MySQL 自带的日志模块式 **binlog（归档日志）** ，所有的存储引擎都可以使用， InnoDB 引擎还自带了一个日志模块 **redo log（重做日志）**。流程如下：

   - 先查询到数据，如果有缓存，也是会用到缓存。
   - 然后拿到查询的语句，把 进行修改，然后调用引擎 API 接口，写入这一行数据，InnoDB 引擎把数据保存在内存中，同时记录 redo log，此时 redo log 进入 prepare 状态，然后告诉执行器，执行完成了，随时可以提交。
   - 执行器收到通知后记录 binlog，然后调用引擎接口，提交 redo log 为提交状态。
   - 更新完成。

5. 为什么 redo log 要引入 prepare 预提交状态

   - **先写 redo log 直接提交，然后写 binlog**，假设写完 redo log 后，机器挂了，binlog 日志没有被写入，那么机器重启后，这台机器会通过 redo log 恢复数据，但是这个时候 bingog 并没有记录该数据，后续进行机器备份的时候，就会丢失这一条数据，同时主从同步也会丢失这一条数据。
   - **先写 binlog，然后写 redo log**，假设写完了 binlog，机器异常重启了，由于没有 redo log，本机是无法恢复这一条记录的，但是 binlog 又有记录，那么和上面同样的道理，就会产生数据不一致的情况。

   如果采用 redo log 两阶段提交的方式就不一样了，写完 binglog 后，然后再提交 redo log 就会防止出现上述的问题，从而保证了数据的一致性。但假设 redo log 处于预提交状态，binglog 也已经写完了，这个时候发生了异常重启会怎么样呢？ 这个就要依赖于 MySQL 的处理机制了，MySQL 的处理过程如下：

   - 判断 redo log 是否完整，如果判断是完整的，就立即提交。
   - 如果 redo log 只是预提交但不是 commit 状态，这个时候就会去判断 binlog 是否完整，如果完整就提交 redo log, 不完整就回滚事务。

   这样就解决了数据一致性的问题。

6. **总结**

   - MySQL 主要分为 Server 层和引擎层，Server 层主要包括连接器、查询缓存、分析器、优化器、执行器，同时还有一个日志模块（binlog），这个日志模块所有执行引擎都可以共用,redolog 只有 InnoDB 有。
   - 引擎层是插件式的，目前主要包括，MyISAM,InnoDB,Memory 等。
   - SQL 等执行过程分为两类，一类对于查询等过程如下：权限校验--->查询缓存--->分析器--->优化器--->权限校验--->执行器--->引擎
   - 对于更新等语句执行流程如下：分析器---->权限校验---->执行器--->引擎---redo log prepare--->binlog--->redo log commit

### 10.日志文件

Mysql共有7种日志文件：

- **redo log(重做日志)**

  1. **作用：**确保事务的持久性。

     防止在发生故障的时间点，尚有脏页未写入磁盘，在重启mysql服务的时候，根据redo log进行重做，从而达到事务的持久性这一特性。

  2. **内容：**物理格式的日志，记录的是物理数据页面的修改的信息，其redo log是顺序写入redo log file的物理文件中去的。

  3. **何时产生：**事务开始之后就产生redo log，redo log的落盘并不是随着事务的提交才写入的，而是在事务的执行过程中，便开始写入redo log文件中。

  4. **何时释放：**当对应事务的脏页写入到磁盘之后，redo log的使命也就完成了，重做日志占用的空间就可以重用（被覆盖）。

  5. **何时写磁盘：**重做日志有一个缓存区**Innodb_log_buffer**，Innodb_log_buffer的默认大小为8M(这里设置的16M),Innodb存储引擎**先将重做日志写入innodb_log_buffer**中。

     然后会通过以下三种方式将innodb日志缓冲区的日志刷新到磁盘

     1. Master Thread 每秒一次执行刷新Innodb_log_buffer到重做日志文件。
     2. 每个事务提交时会将重做日志刷新到重做日志文件。
     3. 当重做日志缓存可用空间 少于一半时，重做日志缓存被刷新到重做日志文件

- **undo log(回滚)**

  1. **作用：**保存了事务发生之前的数据的一个版本，可以用于回滚，同时可以提供多版本并发控制下的读（MVCC），也即非锁定读

  2. **内容：**逻辑格式的日志，在执行undo的时候，仅仅是将数据从逻辑上恢复至事务之前的状态，而不是从物理页面上操作实现的，这一点是不同于redo log的。

  3. **何时产生：**事务开始之前，将当前的版本生成undo log，undo 也会产生 redo 来保证undo log的可靠性

  4. **何时释放：**当事务提交之后，undo log并不能立马被删除，而是放入待清理的链表，由purge线程判断是否由其他事务在使用undo段中表的上一个事务之前的版本信息，决定是否可以清理undo log的日志空间。

  5. **对应文件：**innodb_undo_directory = /data/undospace/ –undo独立表空间的存放目录

     innodb_undo_logs = 128 –回滚段为128KB

     innodb_undo_tablespaces = 4 –指定有4个undo log文件

     如果undo使用的共享表空间，这个共享表空间中又不仅仅是存储了undo的信息，共享表空间的默认为与MySQL的数据目录下面，其属性由参数innodb_data_file_path配置。

     mysql5.7之后有“独立undo 表空间”的配置。

- **bin log(二进制)**

  1. **作用：**用于复制，在主从复制中，从库利用主库上的binlog进行重播，实现主从同步；用于数据库的基于时间点的还原；

  2. **内容：**逻辑格式的日志，可以简单认为就是执行过的事务中的sql语句。

     但又不完全是sql语句这么简单，而是包括了执行的sql语句（增删改）反向的信息，也就意味着delete对应着delete本身和其反向的insert；update对应着update执行前后的版本的信息；insert对应着delete和insert本身的信息。

  3. **何时产生：**事务提交的时候，一次性将事务中的sql语句（一个事物可能对应多个sql语句）按照一定的格式记录到binlog中。

     这里与redo log很明显的差异就是redo log并不一定是在事务提交的时候刷新到磁盘，redo log是在事务开始之后就开始逐步写入磁盘。

     因此对于事务的提交，即便是较大的事务，提交（commit）都是很快的，但是在开启了bin_log的情况下，对于较大事务的提交，可能会变得比较慢一些。

  4. **何时释放：**binlog的默认是保持时间由参数expire_logs_days配置，也就是说对于非活动的日志文件，在生成时间超过expire_logs_days配置的天数之后，会被自动删除。

  5. **对应物理文件：**配置文件的路径为log_bin_basename，binlog日志文件按照指定大小，当日志文件达到指定的最大的大小之后，进行滚动更新，生成新的日志文件。

     对于每个binlog日志文件，通过一个统一的index文件来组织。

  6. **与redo log比较：**

     - 作用不同：redo log是保证事务的持久性的，是事务层面的，binlog作为还原的功能，是数据库层面的（当然也可以精确到事务层面的），虽然都有还原的意思，但是其保护数据的层次是不一样的。
     - 内容不同：redo log是物理日志，是数据页面的修改之后的物理记录，binlog是逻辑日志，可以简单认为记录的就是sql语句
     - 另外，两者日志产生的时间，可以释放的时间，在可释放的情况下清理机制，都是完全不同的。
     - 恢复数据时候的效率，基于物理日志的redo log恢复数据的效率要高于语句逻辑日志的binlog

- **error log(错误)**

  记录启动、运行或停止mysqld时出现的问题。

- **slowquery log(慢查询)**

  记录所有执行时间超过long_query_time秒的所有查询或不使用索引的查询。

- **general log(一般查询)**

  记录建立的客户端连接和执行的语句。

- **relay log(中继)**

### 11.索引下推

**索引下推（Index Condition Pushdown (ICP) ）是MySQL5.6添加的，用于优化数据查询。**

- 不使用索引条件下推优化时存储引擎通过索引检索到数据，然后返回给MySQL服务器，服务器然后判断数据是否符合条件。
- **当使用索引条件下推优化时，如果存在某些被索引的列的判断条件时，MySQL服务器将这一部分判断条件传递给存储引擎，然后由存储引擎通过判断索引是否符合MySQL服务器传递的条件，只有当索引符合条件时才会将数据检索出来返回给MySQL服务器。**索引条件下推优化可以减少存储引擎查询基础表的次数，也可以减少MySQL服务器从存储引擎接收数据的次数。

1. **适用条件：**
   1. 需要整表扫描的情况。比如：range, ref, eq_ref, ref_or_null 。适用于InnoDB 引擎和 MyISAM 引擎的查询。（5.6版本不适用分区表查询，5.7版本后可以用于分区表查询）。
   2. 对于InnDB引擎只适用于二级索引，因为InnDB的聚簇索引会将整行数据读到InnDB的缓冲区，这样一来索引条件下推的主要目的减少IO次数就失去了意义。因为数据已经在内存中了，不再需要去读取了。
   3. 引用子查询的条件不能下推。
   4. 调用存储过程的条件不能下推，存储引擎无法调用位于MySQL服务器中的存储过程。
   5. 触发条件不能下推。
2. **不使用索引条件下推优化时的查询过程**
   - 获取下一行，首先读取索引信息，然后根据索引将整行数据读取出来。
   - 然后通过where条件判断当前数据是否符合条件，符合返回数据。
3. **使用索引条件下推优化时的查询过程**
   - 获取下一行的索引信息。
   - 检查索引中存储的列信息是否符合索引条件，如果符合将整行数据读取出来，如果不符合跳过读取下一行。
   - 用剩余的判断条件，判断此行数据是否符合要求，符合要求返回数据。

### 12.MySQL慢查询

1. Mysql提供的一种日志记录，用来记录Mysql中响应时间超过阈值的语句。具体环境中，运行时间超过long_query_time的sql语句会被记录。默认为10秒。默认情况下，Mysql数据库并不启动慢查询日志，需要我们手动来设置这个参数，当然，如果不是调优需要的话，一般不建议启动该参数，因为开启慢查询日志会或多或少带来一定的性能影响。慢查询日志支持将日志记录写入文件，也支持将日志记录写入数据库表。

2. 分析慢查询日志：

   **直接分析mysql慢查询日志 ,利用explain关键字可以模拟优化器执行SQL查询语句，来分析sql慢查询语句**

     例如：执行EXPLAIN SELECT * FROM res_user ORDER BYmodifiedtime LIMIT 0,1000

   结果：  table | type | possible_keys | key |key_len | ref | rows | Extra EXPLAIN列的解释：

    table         显示这一行的数据是关于哪张表的      

   **type**         这是重要的列，显示连接使用了何种类型。从最好到最差的连接类型为const、eq_reg、ref、range、indexhe和ALL 

    **rows**        显示需要扫描行数

   **key**          使用的索引

### 13.索引优化

1. **索引没起作用：**

   1. **where查询时使用 or 或者 != 时不走索引**
   2. **like查询时，“%”放前面时不走索引**
      - **Like %xx% 不走索引**
      - **Like xx% 走索引**
   3. **where 查询时数据类型不匹配不走索引**
   4. **正则不走索引**
   5. MySQL可以为多个字段创建索引。一个索引最多可以包括16个字段。**对于多列索引，只有查询条件使用了这些字段中的第一个字段时，索引才会被使用。**

2. **优化语句：**

   1. **添加联合索引并且最左前缀匹配**
   2. **主键、外键建立Index**
   3. **where, on, group by, order by建立索引**
   4. **为较长的索引使用前缀索引**
   5. **尽量扩展索引，减少新建**

3. **分解关联查询**

   **将一个大的查询分解为多个小查询是很有必要的。**

    很多高性能的应用都会对关联查询进行分解，就是可以对每一个表进行一次单表查询，然后将查询结果在应用程序中进行关联，很多场景下这样会更高效

4. **优化分页查询**

   在系统中需要分页的操作通常会使用limit加上偏移量的方法实现，同时加上合适的order by 子句。**如果有对应的索引，通常效率会不错，否则MySQL需要做大量的文件排序操作。**

### 14.一条SQL语句执行得很慢的原因

1. **大多数情况是正常的，只是偶尔会出现很慢的情况。**

   1. **数据库在刷新脏页**

      当我们要往数据库插入一条数据、或者要更新一条数据的时候，我们知道数据库会在**内存**中把对应字段的数据更新了，但是更新之后，这些更新的字段并不会马上同步持久化到**磁盘**中去，而是把这些更新的记录写入到 redo log 日记中去，等到空闲的时候，在通过 redo log 里的日记把最新的数据同步到**磁盘**中去。

      > 当内存数据页跟磁盘数据页内容不一致的时候，我们称这个内存页为“脏页”。内存数据写入到磁盘后，内存和磁盘上的数据页的内容就一致了，称为“干净页”。

      **刷脏页有下面4种场景（后两种不用太关注“性能”问题）：**

      - **redolog写满了：**redo log 里的容量是有限的，如果数据库一直很忙，更新又很频繁，这个时候 redo log 很快就会被写满了，这个时候就没办法等到空闲的时候再把数据同步到磁盘的，只能暂停其他操作，全身心来把数据同步到磁盘中去的，而这个时候，**就会导致我们平时正常的SQL语句突然执行的很慢**，所以说，数据库在在同步数据到磁盘的时候，就有可能导致我们的SQL语句执行的很慢了。
      - **内存不够用了：**如果一次查询较多的数据，恰好碰到所查数据页不在内存中时，需要申请内存，而此时恰好内存不足的时候就需要淘汰一部分内存数据页，如果是干净页，就直接释放，如果恰好是脏页就需要刷脏页。
      - **MySQL 认为系统“空闲”的时候：**这时系统没什么压力。
      - **MySQL 正常关闭的时候：**这时候，MySQL 会把内存的脏页都 flush 到磁盘上，这样下次 MySQL 启动的时候，就可以直接从磁盘上读数据，启动速度会很快。

   2. **获取不到锁**

      - 在要执行的这条语句，刚好这条语句涉及到的**表**，别人在用，并且加锁了，我们拿不到锁，只能慢慢等待别人释放锁了。或者，表没有加锁，但要使用到的某个一行被加锁了，这个时候，我也没办法啊。
      - 如果要判断是否真的在等待锁，我们可以用 **show processlist**这个命令来查看当前的状态

2. **在数据量不变的情况下，这条SQL语句一直以来都执行的很慢。**

   1. **没用到索引**

      1. **字段没有索引**

         字段上没有索引，只能走全表扫描，所以导致这条查询语句很慢。

      2. **字段有索引，但却没有用索引**

         ```sql
         //不走索引
         select * from t where c - 1 = 1000;
         //走索引
         select * from t where c = 1000 + 1;
         ```

         如果我们在字段的左边做了运算，那么在查询的时候，就不会用上索引了，所以**字段上有索引，但由于自己的疏忽，会导致系统没有使用索引**

      3. **函数操作导致没有用上索引**

         如果我们在查询的时候，对字段进行了函数操作，也是会导致没有用上索引的，例如

         ```sql
         select * from t where pow(c,2) = 1000;
         ```

   2. **数据库自己选错索引**

3. **总结**

   1. 大多数情况下很正常，偶尔很慢，则有如下原因

      (1)、数据库在刷新脏页，例如 redo log 写满了需要同步到磁盘。

      (2)、执行的时候，遇到锁，如表锁、行锁。

   2. 这条 SQL 语句一直执行的很慢，则有如下原因。

      (1)、没有用上索引：例如该字段没有索引；由于对字段进行运算、函数操作导致无法用索引。

      (2)、数据库选错了索引。

### 15.覆盖索引和最左匹配

1. **覆盖索引**

   **如果一个索引包含(或者说覆盖)所有需要查询的字段，我们称其为“覆盖索引”。在Mysql中，如果不是主键索引，叶子节点存储的是主键+列值，最终需要回表查询，即通过主键再次查询。这样会变慢，覆盖索引就是把要查询的列和索引是对应的，于是就不用回表了。**

   例如：建立索引**(id, name)**，此时查询 

   ```sql
   select id, name from user where id = '1' and name = 'abc';
   ```

   要查询的列在叶子节点都存在，于是不做回表操作。

2. **最左匹配**

   MySQL中的索引可以以一定顺序引用多列，这种索引叫作**联合索引**。如User表的name和city加联合索引就是(name,city)。

   **而最左前缀原则指的是，如果查询的时候查询条件精确匹配索引的左边连续一列或几列，则此列就可以被用到。**

   ```sql
   select * from user where name=xx and city=xx ; ／／可以命中索引 
   
   select * from user where name=xx ; // 可以命中索引 
   
   select * from user where city=xx; // 无法命中索引
   ```

   **这里需要注意的是，查询的时候如果两个条件都用上了，但是顺序不同，如 `city= xx and name ＝xx`，那么现在的查询引擎会自动优化为匹配联合索引的顺序，这样是能够命中索引的.**

   **由于最左前缀原则，在创建联合索引时，索引字段的顺序需要考虑字段值去重之后的个数，较多的放前面。ORDERBY子句也遵循此规则。**

### 16.Mysql主从

MySQL之间数据复制的基础是**二进制日志文件**（binary log file）。**一台MySQL数据库一旦启用二进制日志后，其作为master**，它的数据库中所有操作都会以“事件”的方式记录在二进制日志中，其他数据库作为slave通过一个I/O线程与主服务器保持通信，并监控master的二进制日志文件的变化，如果发现master二进制日志文件发生变化，则会把变化复制到自己的**中继日志(relay log)**中，然后slave的一个SQL线程会把相关的“事件”执行到自己的数据库中，以此实现从数据库和主数据库的一致性，也就实现了主从复制。

![mysql主从复制](/Users/zhangyi/Desktop/Java复习图/mysql主从复制.jpeg)

**Mysql主从复制 (异步复制) 分三步：**

1. master将改变记录到二进制日志（binary log）,这些记录过程叫做二进制日志时
2. slave将master的binary log events 拷贝到它的中继日志（relay log）。
3. salve 重做中继日志中的事件，将改变应用到自己的数据库中，Mysql复制是异步且串行化的。

步骤1和步骤3之间是异步进行的，无需等待确认各自的状态，所以说MySQL replication是异步的。

**复制的基本原则：**

1. 每个slave只有一个master,每个slave只能有唯一的一个服务器id,每个master可以有多个slave。
2. mysql的版本一致，且相互之间网络互通（ping）

**Mysql半同步复制：**

1. master和至少一个slave都要启用semi-sync replication模式；
2. 某个slave连接到master时，会主动告知当前自己是否处于semi-sync模式；
3. 在master上提交事务后，写入binlog后，还需要通知至少一个slave收到该事务，等待写入relay log并成功刷新到磁盘后，向master发送“slave节点已完成该事务”确认通知；
4. master收到上述通知后，才可以真正完成该事务提交，返回事务成功标记；

**在上述步骤中，当slave向master发送通知时间超过rpl_semi_sync_master_timeout设定值时，主从关系会从semi-sync模式自动调整成为传统的异步复制模式。**

**Mysql全同步复制：**

**指当主库执行完一个事务，所有的从库都执行了该事务才返回给客户端。**因为需要等待所有从库执行完该事务才能返回，所以全同步复制的性能必然会收到严重的影响。



# Redis

### 1.Redis简介

 **Redis 就是一个使用 C 语言开发的数据库**，不过与传统数据库不同的是 **Redis 的数据是存在内存中的** ，也就是它是内存数据库，所以读写速度非常快，因此 Redis 被广泛应用于缓存方向。

**Redis 除了做缓存之外，Redis 也经常用来做分布式锁，甚至是消息队列。**

**Redis 提供了多种数据类型来支持不同的业务场景。Redis 还支持事务 、持久化、Lua 脚本、多种集群方案。**

### 2.Redis和Memcache区别和共同点

**共同点** ：

1. 都是基于内存的数据库，一般都用来当做缓存使用。
2. 都有过期策略。
3. 两者的性能都非常高。

**区别** ：

1. **Redis 支持更丰富的数据类型（支持更复杂的应用场景）**。Redis 不仅仅支持简单的 k/v 类型的数据，同时还提供 list，set，zset，hash 等数据结构的存储。Memcached 只支持最简单的 k/v 数据类型。
2. **Redis 支持数据的持久化，可以将内存中的数据保持在磁盘中，重启的时候可以再次加载进行使用,而 Memecache 把数据全部存在内存之中。**
3. **Redis 有灾难恢复机制。** 因为可以把缓存中的数据持久化到磁盘上。
4. **Redis 在服务器内存使用完之后，可以将不用的数据放到磁盘上。但是，Memcached 在服务器内存使用完之后，就会直接报异常。**
5. **Memcached 没有原生的集群模式，需要依靠客户端来实现往集群中分片写入数据；但是 Redis 目前是原生支持 cluster 模式的.**
6. **Memcached 是多线程，非阻塞 IO 复用的网络模型；Redis 使用单线程的多路 IO 复用模型。** （Redis 6.0 引入了多线程 IO ）
7. **Redis 支持发布订阅模型、Lua 脚本、事务等功能，而 Memcached 不支持。并且，Redis 支持更多的编程语言。**
8. **Memcached过期数据的删除策略只用了惰性删除，而 Redis 同时使用了惰性删除与定期删除。**

### 3.缓存数据的处理流程

![缓存数据处理流程](/Users/zhangyi/Desktop/Java复习图/缓存数据处理流程.png)

简单来说就是:

1. 如果用户请求的数据在缓存中就直接返回。
2. 缓存中不存在的话就看数据库中是否存在。
3. 数据库中存在的话就更新缓存中的数据。
4. 数据库中不存在的话就返回空数据。

### 4.Redis为什么快

1. **Redis是基于内存的，内存的读写速度非常快；**

2. **Redis是单线程的，省去了很多上下文切换线程的时间；**

3. **Redis使用的是非阻塞IO，IO多路复用，使用了单线程来轮询描述符，将数据库的开、关、读、写都转换成了事件，减少了线程切换时上下文的切换和竞争。**

   redis使用I/O多路复用技术，可以处理并发的连接。非阻塞IO 内部实现采用epoll，采用了epoll+自己实现的简单的事件框架。epoll中的读、写、关闭、连接都转化成了事件，然后利用epoll的多路复用特性，绝不在io上浪费一点时间。

4. **数据结构也帮了不少忙，Redis全程使用hash结构，读取速度快，还有一些特殊的数据结构，对数据存储进行了优化，如压缩表，对短数据进行压缩存储，再如，跳表，使用有序的数据结构加快读取的速度。**

5. **Redis采用自己实现的事件分离器，效率比较高，内部采用非阻塞的执行方式，吞吐能力比较大。**

### 5.Redis常用数据结构

**Redis中的数据类型有：string、hash、list、set、zset（sorted set）**

1. **string**

   1. **介绍** ：string 数据结构是简单的 key-value 类型。虽然 Redis 是用 C 语言写的，但是 Redis 并没有使用 C 的字符串表示，而是自己构建了一种 **简单动态字符串**（simple dynamic string，**SDS**）。相比于 C 的原生字符串，Redis 的 SDS 不光可以保存文本数据还可以保存二进制数据，并且获取字符串长度复杂度为 O(1)（C 字符串为 O(N)）,除此之外,Redis 的 SDS API 是安全的，不会造成缓冲区溢出。
   2. **常用命令:** `set,get,strlen,exists,dect,incr,setex` 等等。批量设置用`mset, mget`
   3. **应用场景** ：一般常用在需要计数的场景，比如用户的访问次数、热点文章的点赞转发数量等等。

2. **list**

   1. **介绍** ：**list** 即是 **链表**。链表是一种非常常见的数据结构，特点是易于数据元素的插入和删除并且且可以灵活调整链表长度，但是链表的随机访问困难。许多高级编程语言都内置了链表的实现比如 Java 中的 **LinkedList**，但是 C 语言并没有实现链表，所以 Redis 实现了自己的链表数据结构。Redis 的 list 的实现为一个 **双向链表**，即可以支持反向查找和遍历，更方便操作，不过带来了部分额外的内存开销。

   2. **常用命令:** `rpush,lpop,lpush,rpop,lrange、llen` 等。**通过 `rpush/rpop` 实现栈：**

   3. **应用场景:** 发布与订阅或者说消息队列、慢查询。

      ![redis的list](/Users/zhangyi/Desktop/Java复习图/redis的list.jpg)

3. **hash**

   1. **介绍** ：hash 类似于 JDK1.8 前的 HashMap，内部实现也差不多(数组 + 链表)。不过，Redis 的 hash 做了更多优化。另外，hash 是一个 string 类型的 field 和 value 的映射表，**特别适合用于存储对象**，后续操作的时候，你可以直接仅仅修改这个对象中的某个字段的值。 比如我们可以 hash 数据结构来存储用户信息，商品信息等等。
   2. **常用命令：** `hset,hmset,hexists,hget,hgetall,hkeys,hvals` 等。
   3. **应用场景:** 系统中对象数据的存储。

4. **set**

   1. **介绍 ：** set 类似于 Java 中的 `HashSet` 。Redis 中的 set 类型是一种无序集合，集合中的元素没有先后顺序。当你需要存储一个列表数据，又不希望出现重复数据时，set 是一个很好的选择，并且 set 提供了判断某个成员是否在一个 set 集合内的重要接口，这个也是 list 所不能提供的。可以基于 set 轻易实现交集、并集、差集的操作。比如：你可以将一个用户所有的关注人存在一个集合中，将其所有粉丝存在一个集合。Redis 可以非常方便的实现如共同关注、共同粉丝、共同喜好等功能。这个过程也就是求交集的过程。
   2. **常用命令：** `sadd,spop,smembers,sismember,scard,sinterstore,sunion` 等。
   3. **应用场景:** 需要存放的数据不能重复以及需要获取多个数据源交集和并集等场景

5. **zset**

   1. **介绍：** 和 set 相比，sorted set 增加了一个权重参数 score，使得集合中的元素能够按 score 进行有序排列，还可以通过 score 的范围来获取元素的列表。有点像是 Java 中 HashMap 和 TreeSet 的结合体。
   2. **常用命令：** `zadd,zcard,zscore,zrange,zrevrange,zrem` 等。
   3. **应用场景：** 需要对数据根据某个权重进行排序的场景。比如在直播系统中，实时排行信息包含直播间在线用户列表，各种礼物排行榜，弹幕消息（可以理解为按消息维度的消息排行榜）等信息。

### **6.跳跃表**

跳跃表：**链表加多级索引的结构，就是跳跃表！**

如果一个有序集合包含的**元素比较多**，或者成员是**比较长的字符串**时，Redis会用跳表作为zset的底层实现。原因是：

**跳跃表在链表的基础上增加了多级索引以提升查找的效率**，但其是一个**空间换时间**的方案，必然会带来一个问题——**索引是占内存的**。原始链表中存储的有可能是很大的对象，而索引结点只需要存储关键值值和几个指针，并不需要存储对象，因此当节点本身比较大或者元素数量比较多的时候，其优势必然会被放大，而缺点则可以忽略。

Redis的跳跃表由**zskiplistNode和skiplist**两个结构定义,其中 **zskiplistNode**结构用于表示跳跃表节点,而 **zskiplist结构则用于保存跳跃表节点的相关信息**,比如节点的数量,以及指向表头节点和表尾节点的指针等等。

![Redis跳跃表](/Users/zhangyi/Desktop/Java复习图/Redis跳跃表.png)

   上图展示了一个跳跃表示例,**其中最左边的是 skiplist结构**,该结构包含以下属性。

- **header**: 指向跳跃表的表头节点，通过这个指针程序定位表头节点的时间复杂度就为O(1)
- **tail**: 指向跳跃表的表尾节点,通过这个指针程序定位表尾节点的时间复杂度就为O(1)
- **level**: 记录目前跳跃表内,层数最大的那个节点的层数(表头节点的层数不计算在内)，通过这个属性可以再O(1)的时间复杂度内获取层高最好的节点的层数。
- **length**:记录跳跃表的长度,也即是,跳跃表目前包含节点的数量(表头节点不计算在内)，通过这个属性，程序可以再O(1)的时间复杂度内返回跳跃表的长度。

**结构右方的是四个 zskiplistNode结构,该结构包含以下属性**

- **层(level):**

    节点中用1、2、L3等字样标记节点的各个层,L1代表第一层,L代表第二层,以此类推。

    每个层都带有两个属性:**前进指针和跨度**。前进指针用于访问位于表尾方向的其他节点,而跨度则记录了前进指针所指向节点和当前节点的距离(跨度越大、距离越远)。在上图中,连线上带有数字的箭头就代表前进指针,而那个数字就是跨度。当程序从表头向表尾进行遍历时,访问会沿着层的前进指针进行。

    每次创建一个新跳跃表节点的时候,程序都根据幂次定律(powerlaw,越大的数出现的概率越小)随机生成一个介于1和32之间的值作为level数组的大小,这个大小就是层的“高度”。

- **后退(backward)指针：**

    节点中用BW字样标记节点的后退指针,它指向位于当前节点的前一个节点。后退指针在程序从表尾向表头遍历时使用。与前进指针所不同的是每个节点只有一个后退指针，因此每次只能后退一个节点。

- **分值(score):**

    各个节点中的1.0、2.0和3.0是节点所保存的分值。在跳跃表中,节点按各自所保存的分值从小到大排列。

- **成员对象(oj):**

    各个节点中的o1、o2和o3是节点所保存的成员对象。在同一个跳跃表中,各个节点保存的成员对象必须是唯一的,但是多个节点保存的分值却可以是相同的:分值相同的节点将按照成员对象在字典序中的大小来进行排序,成员对象较小的节点会排在前面(靠近表头的方向),而成员对象较大的节点则会排在后面(靠近表尾的方向)。

**总结**

- 跳跃表基于单链表加索引的方式实现
- 跳跃表以空间换时间的方式提升了查找速度
- Redis有序集合在节点元素较大或者元素数量较多时使用跳跃表实现
- Redis的跳跃表实现由 zskiplist和 zskiplistnode两个结构组成,其中 zskiplist用于保存跳跃表信息(比如表头节点、表尾节点、长度),而zskiplistnode则用于表示跳跃表节点
- Redis每个跳跃表节点的层高都是1至32之间的随机数
- 在同一个跳跃表中,多个节点可以包含相同的分值,但每个节点的成员对象必须是唯一的跳跃表中的节点按照分值大小进行排序,当分值相同时,节点按照成员对象的大小进行排序。

**跳跃表的插入：**

1. 新节点和各层索引节点逐一比较，确定原链表的插入位置。O（logN）
2. 把索引插入到原链表。O（1）
3. 利用抛硬币的随机方式，决定新节点是否提升为上一级索引。结果为“正”则提升并继续抛硬币，结果为“负”则停止。O（logN）

**总体上，跳跃表插入操作的时间复杂度是O（logN），而这种数据结构所占空间是2N，既空间复杂度是 O（N）。**

**跳跃表的删除**

1. 自上而下，查找第一次出现节点的索引，并逐层找到每一层对应的节点。O（logN）
2. 删除每一层查找到的节点，如果该层只剩下1个节点，删除整个一层（原链表除外）。O（logN）

**总体上，跳跃表删除操作的时间复杂度是O（logN）。**

### 7.Redis单线程模型

**Redis 基于 Reactor 模式来设计开发了自己的一套高效的事件处理模型** （Netty 的线程模型也基于 Reactor 模式，Reactor 模式不愧是高性能 IO 的基石），这套事件处理模型对应的是 **Redis 中的文件事件处理器**（file event handler）。由于文件事件处理器（file event handler）是单线程方式运行的，所以我们一般都说 Redis 是单线程模型。

**Redis既然是单线程，那怎么监听大量的客户端连接呢？**

Redis 通过**IO 多路复用程序** 来监听来自客户端的大量连接（或者说是监听多个 socket），它会将感兴趣的事件及类型(读、写）注册到内核中并监听每个事件是否发生。

这样的好处非常明显： **I/O 多路复用技术的使用让 Redis 不需要额外创建多余的线程来监听客户端的大量连接，降低了资源的消耗**（和 NIO 中的 `Selector` 组件很像）。

另外， Redis 服务器是一个事件驱动程序，服务器需要处理两类事件： **1. 文件事件; 2. 时间事件。**

时间事件不需要多花时间了解，我们接触最多的还是 **文件事件**（客户端进行读取写入等操作，涉及一系列网络通信）。

> Redis 基于 Reactor 模式开发了自己的网络事件处理器：这个处理器被称为文件事件处理器（file event handler）。文件事件处理器使用 I/O 多路复用（multiplexing）程序来同时监听多个套接字，并根据 套接字目前执行的任务来为套接字关联不同的事件处理器。
>
> 当被监听的套接字准备好执行连接应答（accept）、读取（read）、写入（write）、关 闭（close）等操作时，与操作相对应的文件事件就会产生，这时文件事件处理器就会调用套接字之前关联好的事件处理器来处理这些事件。
>
> **虽然文件事件处理器以单线程方式运行，但通过使用 I/O 多路复用程序来监听多个套接字**，文件事件处理器既实现了高性能的网络通信模型，又可以很好地与 Redis 服务器中其他同样以单线程方式运行的模块进行对接，这保持了 Redis 内部单线程设计的简单性。

可以看出，文件事件处理器（file event handler）主要是包含 4 个部分：

- 多个 socket（客户端连接）
- IO 多路复用程序（支持多个客户端连接的关键）
- 文件事件分派器（将 socket 关联到相应的事件处理器）
- 事件处理器（连接应答处理器、命令请求处理器、命令回复处理器）

![Redis单线程模型](/Users/zhangyi/Desktop/Java复习图/Redis单线程模型.png)

### 8.Redis为什么不用多线程

**那，Redis6.0 之前 为什么不使用多线程？**

我觉得主要原因有下面 3 个：

1. 单线程编程容易并且更容易维护；
2. Redis 的性能瓶颈不再在CPU ，主要在内存和网络；
3. 多线程就会存在死锁、线程上下文切换等问题，甚至会影响性能。



### **9.Redis6.0后为何引入多线程**

**Redis6.0 引入多线程主要是为了提高网络 IO 读写性能**，因为这个算是 Redis 中的一个性能瓶颈（Redis 的瓶颈主要受限于内存和网络）。

虽然，Redis6.0 引入了多线程，但是 Redis 的多线程**只是在网络数据的读写这类耗时操作上使用了， 执行命令仍然是单线程顺序执行**。因此，你也不需要担心线程安全问题。

Redis6.0 的多线程默认是禁用的，只使用主线程。如需开启需要修改 redis 配置文件 `redis.conf` ：

```bash
io-threads-do-reads yes
```

开启多线程后，还需要设置线程数，否则是不生效的。同样需要修改 redis 配置文件 `redis.conf` :

```bash
io-threads 4 #官网建议4核的机器建议设置为2或3个线程，8核的建议设置为6个线程
```

**Redis 选择使用单线程模型处理客户端的请求主要还是因为 CPU 不是 Redis 服务器的瓶颈，所以使用多线程模型带来的性能提升并不能抵消它带来的开发成本和维护成本，系统的性能瓶颈也主要在网络 I/O 操作上；**

**而 Redis 引入多线程操作也是出于性能上的考虑，对于一些大键值对的删除操作，通过多线程非阻塞地释放内存空间也能减少对 Redis 主线程阻塞的时间，提高执行的效率。**



### 10.Redis给缓存设置过期时间的作用

因为内存是有限的，如果缓存中的所有数据都是一直保存的话，最终直接Out of memory。Redis 自带了给缓存数据设置过期时间的功能，比如：

```bash
127.0.0.1:6379> exp key  60 # 数据在 60s 后过期
(integer) 1
127.0.0.1:6379> setex key 60 value # 数据在 60s 后过期 (setex:[set] + [ex]pire)
OK
127.0.0.1:6379> ttl key # 查看数据还有多久
```

注意：**Redis中除了字符串类型有自己独有设置过期时间的命令 `setex` 外，其他方法都需要依靠 `expire` 命令来设置过期时间 。另外， `persist` 命令可以移除一个键的过期时间。**

**过期时间除了有助于缓解内存的消耗，还有什么其他用么？**

很多时候业务场景就是需要某个数据只在某一时间段内存在，比如我们的短信验证码可能只在1分钟内有效，用户登录的 token 可能只在 1 天内有效。

如果用传统的数据库处理的话，一般都是自己判断过期，这样更麻烦且性能要差很多。



### **11.Redis如何判断数据是否过期**

Redis 通过一个叫做**过期字典**（可以看作是hash表）来保存数据过期的时间。**过期字典的键指向Redis数据库中的某个key(键)，过期字典的值是一个long long类型的整数，这个整数保存了key所指向的数据库键的过期时间（毫秒精度的UNIX时间戳）。**

![Redis过期字典](/Users/zhangyi/Desktop/Java复习图/Redis过期字典.png)

过期字典是存储在redisDb这个结构里的：

```c
typedef struct redisDb {
    ...
    
    dict *dict;     //数据库键空间,保存着数据库中所有键值对
    dict *expires   // 过期字典,保存着键的过期时间
    ...
} redisDb;
```

### 12.Redis过期数据删除策略

常用的过期数据的删除策略就两个：

1. **惰性删除** ：只会在取出key的时候才对数据进行过期检查。这样对CPU最友好，但是可能会造成太多过期 key 没有被删除。
2. **定期删除** ： 每隔一段时间抽取一批 key 执行删除过期key操作。并且，Redis 底层会通过限制删除操作执行的时长和频率来减少删除操作对CPU时间的影响。

定期删除对内存更加友好，惰性删除对CPU更加友好。两者各有千秋，所以Redis 采用的是 **定期删除+惰性/懒汉式删除** 。

但是，仅仅通过给 key 设置过期时间还是有问题的。因为还是可能存在定期删除和惰性删除漏掉了很多过期 key 的情况。这样就导致大量过期 key 堆积在内存里，然后就Out of memory了。

怎么解决这个问题呢？答案就是： **Redis 内存淘汰机制。**

### 13.Redis内存淘汰机制

相关问题：MySQL 里有 2000w 数据，Redis 中只存 20w 的数据，如何保证 Redis 中的数据都是热点数据?

Redis 提供 6 种数据淘汰策略：

1. **volatile-lru（least recently used）**：从已设置过期时间的数据集（server.db[i].expires）中挑选最近最少使用的数据淘汰
2. **volatile-ttl**：从已设置过期时间的数据集（server.db[i].expires）中挑选将要过期的数据淘汰
3. **volatile-random**：从已设置过期时间的数据集（server.db[i].expires）中任意选择数据淘汰
4. **allkeys-lru（least recently used）**：当内存不足以容纳新写入数据时，在键空间中，移除最近最少使用的 key（这个是最常用的）
5. **allkeys-random**：从数据集（server.db[i].dict）中任意选择数据淘汰
6. **no-eviction**：禁止驱逐数据，也就是说当内存不足以容纳新写入数据时，新写入操作会报错。这个应该没人使用吧！

### 14.Redis持久化机制

很多时候我们需要持久化数据也就是将内存中的数据写入到硬盘里面，大部分原因是为了之后重用数据（比如重启机器、机器故障之后恢复数据），或者是为了防止系统故障而将数据备份到一个远程位置。

**Redis 的一种持久化方式叫快照（snapshotting，RDB），另一种方式是只追加文件（append-only file, AOF）**。

1. **快照（snapshotting）持久化（RDB）**

   Redis 可以通过创建快照来获得存储在内存里面的数据在某个时间点上的副本。Redis 创建快照之后，**可以对快照进行备份，可以将快照复制到其他服务器从而创建具有相同数据的服务器副本**（Redis 主从结构，主要用来提高 Redis 性能），还可以将快照留在原地以便重启服务器的时候使用。

   **快照持久化(RDB)是 Redis 默认采用的持久化方式**，在 Redis.conf 配置文件中默认有此下配置：

   ```conf
   save 900 1           #在900秒(15分钟)之后，如果至少有1个key发生变化，Redis就会自动触发BGSAVE命令创建快照。
   
   save 300 10          #在300秒(5分钟)之后，如果至少有10个key发生变化，Redis就会自动触发BGSAVE命令创建快照。
   
   save 60 10000        #在60秒(1分钟)之后，如果至少有10000个key发生变化，Redis就会自动触发BGSAVE命令创建快照。
   ```

2. **AOF（append-only file）持久化**

   与快照持久化相比，AOF 持久化 的实时性更好，因此已成为主流的持久化方案。默认情况下 Redis 没有开启 AOF（append only file）方式的持久化，可以通过 appendonly 参数开启：

   ```conf
   appendonly yes
   ```

   **开启 AOF 持久化后每执行一条会更改 Redis 中的数据的命令，Redis 就会将该命令写入硬盘中的 AOF 文件。AOF 文件的保存位置和 RDB 文件的位置相同，都是通过 dir 参数设置的，默认的文件名是 appendonly.aof。**

   在 Redis 的配置文件中存在三种不同的 AOF 持久化方式，它们分别是：

   ```conf
   appendfsync always    #每次有数据修改发生时都会写入AOF文件,这样会严重降低Redis的速度
   appendfsync everysec  #每秒钟同步一次，显示地将多个写命令同步到硬盘
   appendfsync no        #让操作系统决定何时进行同步
   ```

   为了兼顾数据和写入性能，**用户可以考虑 appendfsync everysec 选项 ，让 Redis 每秒同步一次 AOF 文件，Redis 性能几乎没受到任何影响**。而且这样即使出现系统崩溃，用户最多只会丢失一秒之内产生的数据。当硬盘忙于执行写入操作的时候，Redis 还会优雅的放慢自己的速度以便适应硬盘的最大写入速度。

**Redis 4.0 开始支持 RDB 和 AOF 的混合持久化（默认关闭，可以通过配置项 `aof-use-rdb-preamble` 开启）。**

- **如果把混合持久化打开，AOF 重写的时候就直接把 RDB 的内容写到 AOF 文件开头。这样做的好处是可以结合 RDB 和 AOF 的优点, 快速加载同时避免丢失过多的数据。当然缺点也是有的， AOF 里面的 RDB 部分是压缩格式不再是 AOF 格式，可读性较差。**

- **RDB和AOF混合使用：**

  RDB为一个表的全量数据，AOF为每次操作的日志。服务器重启是，先用RDB，恢复全量数据，然后AOF回放日志，数据完整了。

**RDB原理：fork和CopyOnWrite**

fork创建子进程进行RDB，子进程创建之后，父子进程共享数据段，父进程继续提供读写。

**AOF 重写**

AOF 重写可以产生一个新的 AOF 文件，这个新的 AOF 文件和原有的 AOF 文件所保存的数据库状态一样，但体积更小。

AOF 重写是一个有歧义的名字，该功能是通过读取数据库中的键值对来实现的，程序无须对现有 AOF 文件进行任何读入、分析或者写入操作。

在执行 BGREWRITEAOF 命令时，Redis 服务器会维护一个 AOF 重写缓冲区，该缓冲区会在子进程创建新 AOF 文件期间，记录服务器执行的所有写命令。当子进程完成创建新 AOF 文件的工作之后，服务器会将重写缓冲区中的所有内容追加到新 AOF 文件的末尾，使得新旧两个 AOF 文件所保存的数据库状态一致。最后，服务器用新的 AOF 文件替换旧的 AOF 文件，以此来完成 AOF 文件重写操作



### 15.Redis事务

Redis 可以通过 **MULTI，EXEC，DISCARD 和 WATCH** 等命令来实现事务(transaction)功能。

```bash
> MULTI
OK
> INCR foo
QUEUED
> INCR bar
QUEUED
> EXEC
1) (integer) 1
2) (integer) 1
```

使用 **MULTI**命令后可以输入多个命令。Redis不会立即执行这些命令，而是将它们放到队列，当调用了**EXEC**命令将执行所有命令。

但是，Redis 的事务和我们平时理解的关系型数据库的事务不同。我们知道事务具有四大特性： **1. 原子性**，**2. 隔离性**，**3. 持久性**，**4. 一致性**。

1. **原子性（Atomicity）：** 事务是最小的执行单位，不允许分割。事务的原子性确保动作要么全部完成，要么完全不起作用；
2. **隔离性（Isolation）：** 并发访问数据库时，一个用户的事务不被其他事务所干扰，各并发事务之间数据库是独立的；
3. **持久性（Durability）：** 一个事务被提交之后。它对数据库中数据的改变是持久的，即使数据库发生故障也不应该对其有任何影响。
4. **一致性（Consistency）：** 执行事务前后，数据保持一致，多个事务对同一个数据读取的结果是相同的；

**Redis 是不支持 roll back 的，因而不满足原子性的（而且不满足持久性）。**

Redis官网也解释了自己为啥不支持回滚。简单来说就是Redis开发者们觉得没必要支持回滚，这样更简单便捷并且性能更好。**Redis开发者觉得即使命令执行错误也应该在开发过程中就被发现而不是生产过程中。**

你可以将Redis中的事务就理解为 ：**Redis事务提供了一种将多个命令请求打包的功能。然后，再按顺序执行打包的所有命令，并且不会被中途打断。**

### 16.缓存穿透

**缓存穿透说简单点就是大量请求的 key 根本不存在于缓存中，导致请求直接到了数据库上，根本没有经过缓存这一层。**举个例子：某个黑客故意制造我们缓存中不存在的 key 发起大量请求，导致大量请求落到数据库。

- **缓存穿透的情况流程：**

  <img src="/Users/zhangyi/Desktop/Java复习图/缓存穿透的情况流程.png" alt="缓存穿透的情况流程" style="zoom: 67%;" />

- **缓存穿透解决方法**

  最基本的就是首先做好参数校验，一些不合法的参数请求直接抛出异常信息返回给客户端。比如查询的数据库 id 不能小于 0、传入的邮箱格式不对的时候直接返回错误消息给客户端、用户鉴权校验，参数做校验，不合法的参数直接代码Return，比如：id 做基础校验，id <=0的直接拦截等等。

  1. **缓存无效 key**

     **如果缓存和数据库都查不到某个 key 的数据就写一个到 Redis 中去并设置过期时间，具体命令如下： `SET key value EX 10086` 。**这种方式可以解决请求的 key 变化不频繁的情况，如果黑客恶意攻击，每次构建不同的请求 key，会导致 Redis 中缓存大量无效的 key 。很明显，这种方案并不能从根本上解决此问题。如果非要用这种方式来解决穿透问题的话，尽量将无效的 key 的过期时间设置短一点比如 1 分钟。

     **一般情况下我们是这样设计 key 的： `表名:列名:主键名:主键值` 。**

     如果用 Java 代码展示的话，差不多是下面这样的：

     ```java
     public Object getObjectInclNullById(Integer id) {
         // 从缓存中获取数据
         Object cacheValue = cache.get(id);
         // 缓存为空
         if (cacheValue == null) {
             // 从数据库中获取
             Object storageValue = storage.get(key);
             // 缓存空对象
             cache.set(key, storageValue);
             // 如果存储数据为空，需要设置一个过期时间(300秒)
             if (storageValue == null) {
                 // 必须设置过期时间，否则有被攻击的风险
                 cache.expire(key, 60 * 5);
             }
             return storageValue;
         }
         return cacheValue;
     }
     ```

  2. **布隆过滤器(基于Bitmap)**

     布隆过滤器是一个非常神奇的数据结构，通过它我们可以非常方便地判断一个给定数据是否存在于海量数据中。我们需要的就是判断 key 是否合法，有没有感觉布隆过滤器就是我们想要找的那个“人”。

     具体是这样做的：把所有可能存在的请求的值都存放在布隆过滤器中，当用户请求过来，先判断用户发来的请求的值是否存在于布隆过滤器中。不存在的话，直接返回请求参数错误信息给客户端，存在的话才会走下面的流程。

     加入布隆过滤器之后的缓存处理流程图如下。

     ![布隆过滤器](/Users/zhangyi/Desktop/Java复习图/布隆过滤器.png)

     但是，需要注意的是布隆过滤器可能会存在误判的情况。总结来说就是： **布隆过滤器说某个元素存在，小概率会误判。布隆过滤器说某个元素不在，那么这个元素一定不在。**

     **当一个元素加入布隆过滤器中的时候，会进行哪些操作：**

     1. 使用布隆过滤器中的哈希函数对元素值进行计算，得到哈希值（有几个哈希函数得到几个哈希值）。
     2. 根据得到的哈希值，在位数组中把对应下标的值置为 1。

     **当我们需要判断一个元素是否存在于布隆过滤器的时候，会进行哪些操作：**

     1. 对给定元素再次进行相同的哈希计算；
     2. 得到值之后判断位数组中的每个元素是否都为 1，如果值都为 1，那么说明这个值在布隆过滤器中，如果存在一个值不为 1，说明该元素不在布隆过滤器中。

     然后，一定会出现这样一种情况：**不同的字符串可能哈希出来的位置相同。** （可以适当增加位数组大小或者调整我们的哈希函数来降低概率）

### 17.缓存击穿

至于**缓存击穿**嘛，这个跟**缓存雪崩**有点像，但是又有一点不一样，缓存雪崩是因为大面积的缓存失效，打崩了DB，而缓存击穿不同的是**缓存击穿是指一个Key非常热点，在不停的扛着大并发，大并发集中对这一个点进行访问，当这个Key在失效的瞬间，持续的大并发就穿破缓存，直接请求数据库，就像在一个完好无损的桶上凿开了一个洞。**

**缓存击穿的解决方案**

key可能会在某些时间点被超高并发地访问，是一种非常“热点”的数据。这个时候，需要考虑一个问题：缓存被“击穿”的问题。

**使用互斥锁(mutex key)**

业界比较常用的做法，是使用mutex。简单地来说，就是在缓存失效的时候（判断拿出来的值为空），不是立即去load db，而是先使用缓存工具的某些带成功操作返回值的操作（比如Redis的SETNX或者Memcache的ADD）去set一个mutex key，当操作返回成功时，再进行load db的操作并回设缓存；否则，就重试整个get缓存的方法。



### 18.缓存雪崩

缓存雪崩的场景：**缓存在同一时间大面积的失效，后面的请求都直接落到了数据库上，造成数据库短时间内承受大量请求。** 这就好比雪崩一样，摧枯拉朽之势，数据库的压力可想而知，可能直接就被这么多请求弄宕机了。

系统的缓存模块出了问题比如宕机导致不可用。造成系统的所有访问，都要走数据库。

还有一种缓存雪崩的场景是：**有一些被大量访问数据（热点缓存）在某一时刻大面积失效，导致对应的请求直接落到了数据库上。**

**解决方法：**

**针对 Redis 服务不可用的情况：**

1. 采用 Redis 集群，避免单机出现问题整个缓存服务都没办法使用。
2. 限流，避免同时处理大量的请求。

**针对热点缓存失效的情况：**

1. 设置不同的失效时间比如随机设置缓存的失效时间。
2. 缓存永不失效。

### 19.如何保证缓存和数据库一致性

下面单独对 **Cache Aside Pattern（旁路缓存模式）** 来聊聊。

Cache Aside Pattern 中遇到写请求是这样的：**更新 DB，然后直接删除 cache 。**

如果更新数据库成功，而删除缓存这一步失败的情况的话，简单说两个解决方案：

1. **缓存失效时间变短（不推荐，治标不治本）** ：我们让缓存数据的过期时间变短，这样的话缓存就会从数据库中加载数据。另外，这种解决办法对于先操作缓存后操作数据库的场景不适用。
2. **增加cache更新重试机制（常用）**： 如果 cache 服务当前不可用导致缓存删除失败的话，我们就隔一段时间进行重试，重试次数可以自己定。如果多次重试还是失败的话，我们可以把当前更新失败的 key 存入队列中，等缓存服务可用之后，再将 缓存中对应的 key 删除即可。

### 20.Redis发布订阅模式

**publish/subscribe模式可以用做消息队列**

1. client订阅后可以继续订阅或取消订阅、使用ping命令或者结束连接(QUIT)。除这些不能执行其他操作。client将阻塞知道订阅通道上发布的消息到来。
2. 发布的消息在redis中不存储，只能先订阅

### 21.Redis哨兵模式

主从切换：服务器宕机，要手动把一台从服务器切换为主服务器，麻烦。**使用哨兵模式**。

**哨兵是一个单独的进程，原理是哨兵通过发送命令，等待redis服务器响应，从而进行监控。**

**哨兵的两个作用：**

1. **发送命令让redis返回来监控，包括主和从服务器**
2. **哨兵检测到master宕机，自动将slave切换成master，然后通过发布/订阅模式发到其他从服务器，让他们改配置文件，切换主机**

**一个哨兵可能会出现问题，要用多个哨兵，集群最少有3个服务器**

**故障切换：failover**

假设主master宕机，一个哨兵检测到之后系统不会立马failover，而是先主关下线。后面哨兵也检测到主master宕机，达到一定数量之后，哨兵间进行投票，然后一个哨兵进行failover，切换从server为主server，然后通过发布/订阅模式，每个哨兵(sentinel)把监控的slave切换到现在的master，客观下线。

### 22.Redis分布式锁

1. SERNX加锁：

   SET key value NX PX time

   key：键  value：值 

   NX：If Not Exit， 如果不存在才操作，PX：过期，time：过期时间

2. 解锁DEL：

3. 保证原子性，使用LUA脚本

   A持有锁，要运行5s，过期时间4s，第5s B获取锁， A释放时可能释放B持有的，可以判断key对应的value是不是原来的value，但这也不是原子性的，判断和删除是两个步骤，中间可能出错。于是释放redis分布式锁用lua脚本。

### redisson

redisson的锁，就实现了可重入了，但是他的源码比较晦涩难懂。

使用起来很简单，因为他们底层都封装好了，你连接上你的Redis客户端，他帮你做了我上面写的一切，然后更完美。



# Spring



# SpringBoot



# MyBatis



# 设计模式





# 分布式理论



# 计算机网络

![计算机网络体系结构](/Users/zhangyi/Desktop/Java复习图/计算机网络体系结构.png)

### 1.五层协议架构

- **应用层**

  **应用层(application-layer）的任务是通过应用进程间的交互来完成特定网络应用。**应用层协议定义的是应用进程（进程：主机中正在运行的程序）间的通信和交互的规则。对于不同的网络应用需要不同的应用层协议。在互联网中应用层协议很多，如**域名系统DNS**，支持万维网应用的 **HTTP协议**，支持电子邮件的 **SMTP协议**等等。我们把应用层交互的数据单元称为报文。

  **域名系统**（DNS）

  > 域名系统(Domain Name System缩写 DNS，Domain Name被译为域名)是因特网的一项核心服务，它作为可以将域名和IP地址相互映射的一个分布式数据库，能够使人更方便的访问互联网，而不用去记住能够被机器直接读取的IP数串。（百度百科）例如：一个公司的 Web 网站可看作是它在网上的门户，而域名就相当于其门牌地址，通常域名都使用该公司的名称或简称。例如上面提到的微软公司的域名，类似的还有：IBM 公司的域名是 [www.ibm.com、Oracle](http://www.ibm.xn--comoracle-xj3h/) 公司的域名是 [www.oracle.com、Cisco公司的域名是](http://www.oracle.xn--comcisco-hm3g8360a63fhh376bwz2c1gzc/) [www.cisco.com](http://www.cisco.com/) 等。

  **HTTP协议**

  > 超文本传输协议（HTTP，HyperText Transfer Protocol)是互联网上应用最为广泛的一种网络协议。所有的 WWW（万维网） 文件都必须遵守这个标准。设计 HTTP 最初的目的是为了提供一种发布和接收 HTML 页面的方法。（百度百科）

- **传输层**

  **运输层(transport layer)的主要任务就是负责向两台主机进程之间的通信提供通用的数据传输服务**。应用进程利用该服务传送应用层报文。“通用的”是指并不针对某一个特定的网络应用，而是多种应用可以使用同一个运输层服务。由于一台主机可同时运行多个线程，因此运输层有复用和分用的功能。所谓复用就是指多个应用层进程可同时使用下面运输层的服务，分用和复用相反，是运输层把收到的信息分别交付上面应用层中的相应进程。

  **运输层主要使用以下两种协议:**

  1. **传输控制协议 TCP**（Transmission Control Protocol）--提供**面向连接**的，**可靠的**数据传输服务。
  2. **用户数据协议 UDP**（User Datagram Protocol）--提供**无连接**的，尽最大努力的数据传输服务（**不保证数据传输的可靠性**）。

- **网络层**

  **在 计算机网络中进行通信的两个计算机之间可能会经过很多个数据链路，也可能还要经过很多通信子网。网络层的任务就是选择合适的网间路由和交换结点， 确保数据及时传送。** 在发送数据时，网络层把运输层产生的报文段或用户数据报封装成分组和包进行传送。在 TCP/IP 体系结构中，由于网络层使用 **IP 协议**，因此分组也叫 **IP 数据报** ，简称 **数据报**。

  这里要注意：**不要把运输层的“用户数据报 UDP ”和网络层的“ IP 数据报”弄混**。另外，无论是哪一层的数据单元，都可笼统地用“分组”来表示。

- **数据链路层**

  **数据链路层(data link layer)通常简称为链路层。两台主机之间的数据传输，总是在一段一段的链路上传送的，这就需要使用专门的链路层的协议。** 在两个相邻节点之间传送数据时，**数据链路层将网络层交下来的 IP 数据报组装成帧**，在两个相邻节点间的链路上传送帧。每一帧包括数据和必要的控制信息（如同步信息，地址信息，差错控制等）。

- **物理层**

  在物理层上所传送的数据单位是比特。 **物理层(physical layer)的作用是实现相邻计算机节点之间比特流的透明传送，尽可能屏蔽掉具体传输介质和物理设备的差异。** 使其上面的数据链路层不必考虑网络的具体传输介质是什么。“透明传送比特流”表示经实际电路传送后的比特流没有发生变化，对传送的比特流来说，这个电路好像是看不见的。

![七层体系结构图](/Users/zhangyi/Desktop/Java复习图/七层体系结构图.png)



### 2.常用的熟知的端口号

| 应用程序   | FTP   | TFTP | TELNET | SMTP | DNS  | HTTP | SSH  | MYSQL |
| ---------- | ----- | ---- | ------ | ---- | ---- | ---- | ---- | ----- |
| 熟知端口   | 21,20 | 69   | 23     | 25   | 53   | 80   | 22   | 3306  |
| 传输层协议 | TCP   | UDP  | TCP    | TCP  | UDP  | TCP  | TCP  | TCP   |

### 3.TCP三次握手

**最开始的时候客户端和服务器都是处于CLOSED状态。主动打开连接的为客户端，被动打开连接的是服务器。**

1. TCP服务器进程先创建传输控制块TCB，时刻准备接受客户进程的连接请求，此时服务器就进入了**LISTEN（监听）状态**；
2. TCP客户进程也是先创建传输控制块TCB，然后向服务器发出连接请求报文，这是报文首部中的**同部位SYN=1，同时选择一个初始序列号 seq=x** ，此时，TCP客户端进程进入了 **SYN-SENT（同步已发送状态）状态**。TCP规定，SYN报文段（SYN=1的报文段）不能携带数据，但需要消耗掉一个序号。
3. TCP服务器收到请求报文后，如果同意连接，则发出确认报文。**确认报文中应该 ACK=1，SYN=1，确认号是ack=x+1，同时也要为自己初始化一个序列号 seq=y**，此时，TCP服务器进程进入了**SYN-RCVD（同步收到）**状态。这个报文也不能携带数据，但是同样要消耗一个序号。
4. TCP客户进程收到确认后，还要向服务器给出确认。**确认报文的ACK=1，ack=y+1，自己的序列号seq=x+1**，此时，TCP连接建立，客户端进入**ESTABLISHED（已建立连接）**状态。TCP规定，ACK报文段可以携带数据，但是如果不携带数据则不消耗序号。
5. 当服务器收到客户端的确认后也进入ESTABLISHED状态，此后双方就可以开始通信了。

![TCP三次握手](/Users/zhangyi/Desktop/Java复习图/TCP三次握手.png)

#### 1.**为什么要三次握手？**

**三次握手的目的是建立可靠的通信信道，说到通讯，简单来说就是数据的发送与接收，而三次握手最主要的目的就是双方确认自己与对方的发送与接收是正常的。**

第一次握手：Client 什么都不能确认；Server 确认了对方发送正常，自己接收正常

第二次握手：Client 确认了：自己发送、接收正常，对方发送、接收正常；Server 确认了：对方发送正常，自己接收正常

第三次握手：Client 确认了：自己发送、接收正常，对方发送、接收正常；Server 确认了：自己发送、接收正常，对方发送、接收正常

所以三次握手就能确认双发收发功能都正常，缺一不可。

#### 2.为什么要传回SYN

接收端传回发送端所发送的 SYN 是为了告诉发送端，我接收到的信息确实就是你所发送的信号了。

> SYN 是 TCP/IP 建立连接时使用的握手信号。在客户机和服务器之间建立正常的 TCP 网络连接时，客户机首先发出一个 SYN 消息，服务器使用 SYN-ACK 应答表示接收到了这个消息，最后客户机再以 ACK(Acknowledgement[汉译：确认字符 ,在数据通信传输中，接收站发给发送站的一种传输控制字符。它表示确认发来的数据已经接受无误。 ]）消息响应。这样在客户机和服务器之间才能建立起可靠的TCP连接，数据才可以在客户机和服务器之间传递。

#### 3.传了 SYN,为啥还要传 ACK

双方通信无误必须是两者互相发送信息都无误。传了 SYN，证明发送方到接收方的通道没有问题，但是接收方到发送方的通道还需要 ACK 信号来进行验证。

#### 4.为什么TCP客户端最后还要发送一次确认呢？

> 一句话，主要防止已经失效的连接请求报文突然又传送到了服务器，从而产生错误。
>
> 如果使用的是两次握手建立连接，假设有这样一种场景，客户端发送了第一个请求连接并且没有丢失，只是因为在网络结点中滞留的时间太长了，由于TCP的客户端迟迟没有收到确认报文，以为服务器没有收到，此时重新向服务器发送这条报文，此后客户端和服务器经过两次握手完成连接，传输数据，然后关闭连接。此时此前滞留的那一次请求连接，网络通畅了到达了服务器，这个报文本该是失效的，但是，两次握手的机制将会让客户端和服务器再次建立连接，这将导致不必要的错误和资源的浪费。
>
> 如果采用的是三次握手，就算是那一次失效的报文传送过来了，服务端接受到了那条失效报文并且回复了确认报文，但是客户端不会再次发出确认。由于服务器收不到确认，就知道客户端并没有请求连接。

### 4.TCP四次挥手

**数据传输完毕后，双方都可释放连接。最开始的时候，客户端和服务器都是处于ESTABLISHED状态，然后客户端主动关闭，服务器被动关闭。**

1. 客户端进程发出连接释放报文，并且停止发送数据。**释放数据报文首部，FIN=1，其序列号为seq=u（等于前面已经传送过来的数据的最后一个字节的序号加1）**，此时，客户端进入FIN-WAIT-1（终止等待1）状态。 TCP规定，FIN报文段即使不携带数据，也要消耗一个序号。
2. 服务器收到连接释放报文，**发出确认报文，ACK=1，ack=u+1，并且带上自己的序列号seq=v**，此时，服务端就进入了**CLOSE-WAIT（关闭等待）**状态。TCP服务器通知高层的应用进程，客户端向服务器的方向就释放了，这时候**处于半关闭状态，即客户端已经没有数据要发送了，但是服务器若发送数据，客户端依然要接受。这个状态还要持续一段时间，也就是整个CLOSE-WAIT状态持续的时间。**
3. 客户端收到服务器的确认请求后，此时，**客户端就进入FIN-WAIT-2（终止等待2）状态**，等待服务器发送连接释放报文（在这之前还需要接受服务器发送的最后的数据）。
4. 服务器将最后的数据发送完毕后，就向客户端发送连接释放报文，**FIN=1，ack=u+1，**由于在半关闭状态，服务器很可能又发送了一些数据，假定此时的序列号为**seq=w**，此时，服务器就进入了**LAST-ACK（最后确认）**状态，等待客户端的确认。
5. 客户端收到服务器的连接释放报文后，必须发出确认，**ACK=1，ack=w+1，而自己的序列号是seq=u+1**，此时，客户端就进入了**TIME-WAIT（时间等待）状态**。注意此时TCP连接还没有释放，**必须经过2∗ *∗MSL（最长报文段寿命）**的时间后，当客户端撤销相应的TCB后，才进入**CLOSED状态**。
6. 服务器只要收到了客户端发出的确认，立即进入CLOSED状态。同样，撤销TCB后，就结束了这次的TCP连接。可以看到，服务器结束TCP连接的时间要比客户端早一些。

![TCP四次挥手](/Users/zhangyi/Desktop/Java复习图/TCP四次挥手.png)

#### 为什么客户端最后还要等待2MSL？

> MSL（Maximum Segment Lifetime），最大报文生存时间
>
> 第一，**保证客户端发送的最后一个ACK报文能够到达服务器**，因为这个ACK报文可能丢失，站在服务器的角度看来，我已经发送了FIN+ACK报文请求断开了，客户端还没有给我回应，应该是我发送的请求断开报文它没有收到，于是服务器又会重新发送一次，而客户端就能在这个2MSL时间段内收到这个重传的报文，接着给出回应报文，并且会重启2MSL计时器。
>
> 第二，**防止“已经失效的连接请求报文段”出现在本连接中**。客户端发送完最后一个确认报文后，在这个2MSL时间中，就可以使本连接持续的时间内所产生的所有报文段都从网络中消失。这样新的连接中不会出现旧连接的请求报文。

#### 为什么是2MSL?

**去向ACK消息最大存活时间（MSL) + 来向FIN消息的最大存活时间(MSL)。**

客户端发送的ACK segment存活期1MSL,服务端重发FIN segment存活期1MSL，加一起2MSL。2MSL是一个临界值，利用尽量大的等待时间来确保TCP连接断开的可靠性。

#### 为什么建立连接是三次握手，关闭连接是四次挥手呢？

- 建立连接的时候， 服务器在LISTEN状态下，收到建立连接请求的SYN报文后，把ACK和SYN放在一个报文里发送给客户端。
- 而关闭连接时，服务器收到对方的FIN报文时，仅仅表示对方不再发送数据了但是还能接收数据，而自己也未必全部数据都发送给对方了，所以己方可以立即关闭，也可以发送一些数据给对方后，再发送FIN报文给对方来表示同意现在关闭连接，因此，己方ACK和FIN一般都会分开发送，从而导致多了一次。

#### 如果已经建立了连接，但是客户端突然出现故障了怎么办？

> TCP还设有一个保活计时器，显然，客户端如果出现故障，服务器不能一直等下去，白白浪费资源。服务器每收到一次客户端的请求后都会重新复位这个计时器，时间通常是设置为2小时，若两小时还没有收到客户端的任何数据，服务器就会发送一个探测报文段，以后每隔75秒发送一次。若一连发送10个探测报文仍然没反应，服务器就认为客户端出了故障，接着就关闭连接。

### 5.大量time_wait的原因和解决方法

- **原因：**

  **服务器处理完请求后立刻主动正常关闭连接，这样会出现大量socket处于TIME_WAIT状态。如果客户端的并发量持续很高，此时部分客户端就会显示连接不上。**

  **这里就会导致短连接的情况，短连接表示“业务处理+传输数据的时间 远远小于 TIMEWAIT超时的时间”的连接**。

  持续的到达一定量的高并发短连接，会使服务器因端口资源不足而拒绝为一部分客户服务。同时，这些端口都是服务器临时分配，无法用SO_REUSEADDR选项解决这个问题。

- **解决方案**
  1. 调整参数
  2. 保持长连接

### 6.TCP和UDP的区别

1. **TCP 是面向连接，在传送数据之前必须先建立连接，数据传送结束后要释放连接。UDP是无连接的，在传送数据之前不需要先建立连接，远地主机在收到 UDP 报文后，不需要给出任何确认**
2. **TCP是可靠的， UDP不可靠**
3. **TCP只支持点对点通信，UDP支持一对一，一对多，多对多模式**
4. **TCP有拥塞处理机制，UDP没有，适合媒体通信**

### 7.TCP如何保证可靠性传输

1. **TCP 给发送的每一个包进行编号，接收方对数据包进行排序，把有序数据传送给应用层。**
2. **校验和：** TCP 将保持它首部和数据的检验和。这是一个端到端的检验和，目的是检测数据在传输过程中的任何变化。如果接收端的检验和有差错，TCP 将丢弃这个报文段和不确认收到此报文段。
3. **TCP 的接收端会丢弃重复的数据。**
4. **流量控制：** TCP 连接的每一方都有固定大小的缓冲空间，TCP的接收端只允许发送端发送接收端缓冲区能接纳的数据。**当接收方来不及处理发送方的数据，能提示发送方降低发送的速率，防止包丢失。TCP 使用的流量控制协议是可变大小的滑动窗口协议。 （TCP 利用滑动窗口实现流量控制）**
5. **拥塞控制：** 当网络拥塞时，减少数据的发送。
6. **ARQ协议：** 也是为了实现可靠传输的，它的基本原理就是每发完一个分组就停止发送，等待对方确认。在收到确认后再发下一个分组。
7. **超时重传：** 当 TCP 发出一个段后，它启动一个定时器，等待目的端确认收到这个报文段。如果不能及时收到一个确认，将重发这个报文段。

### 8.ARQ协议

**自动重传请求**（Automatic Repeat-reQuest，ARQ）是OSI模型中数据链路层和传输层的错误纠正协议之一。它通过使用**确认和超时这两个机制**，在不可靠服务的基础上实现可靠的信息传输。如果发送方在发送后一段时间之内没有收到确认帧，它通常会重新发送。ARQ包括停止等待ARQ协议和连续ARQ协议。

#### 停止等待ARQ协议

- 停止等待协议是为了实现可靠传输的，它的基本原理就是每发完一个分组就停止发送，等待对方确认（回复ACK）。如果过了一段时间（超时时间后），还是没有收到 ACK 确认，说明没有发送成功，需要重新发送，直到收到确认后再发下一个分组；
- 在停止等待协议中，若接收方收到重复分组，就丢弃该分组，但同时还要发送确认；

**优点：** 简单

**缺点：** 信道利用率低，等待时间长

**1) 无差错情况:**

发送方发送分组,接收方在规定时间内收到,并且回复确认.发送方再次发送。

**2) 出现差错情况（超时重传）:**

停止等待协议中超时重传是指只要超过一段时间仍然没有收到确认，就重传前面发送过的分组（认为刚才发送过的分组丢失了）。因此每发送完一个分组需要设置一个超时计时器，其重传时间应比数据在分组传输的平均往返时间更长一些。这种自动重传方式常称为 **自动重传请求 ARQ** 。另外在停止等待协议中若收到重复分组，就丢弃该分组，但同时还要发送确认。**连续 ARQ 协议** 可提高信道利用率。发送维持一个发送窗口，凡位于发送窗口内的分组可连续发送出去，而不需要等待对方确认。接收方一般采用累积确认，对按序到达的最后一个分组发送确认，表明到这个分组位置的所有分组都已经正确收到了。

**3) 确认丢失和确认迟到**

- **确认丢失** ：确认消息在传输过程丢失。当A发送M1消息，B收到后，B向A发送了一个M1确认消息，但却在传输过程中丢失。而A并不知道，在超时计时过后，A重传M1消息，B再次收到该消息后采取以下两点措施：1. 丢弃这个重复的M1消息，不向上层交付。 2. 向A发送确认消息。（不会认为已经发送过了，就不再发送。A能重传，就证明B的确认消息丢失）。
- **确认迟到** ：确认消息在传输过程中迟到。A发送M1消息，B收到并发送确认。在超时时间内没有收到确认消息，A重传M1消息，B仍然收到并继续发送确认消息（B收到了2份M1）。此时A收到了B第二次发送的确认消息。接着发送其他数据。过了一会，A收到了B第一次发送的对M1的确认消息（A也收到了2份确认消息）。处理如下：1. A收到重复的确认后，直接丢弃。2. B收到重复的M1后，也直接丢弃重复的M1。

#### 连续ARQ协议

连续 ARQ 协议可提高信道利用率。发送方维持一个发送窗口，凡位于发送窗口内的分组可以连续发送出去，而不需要等待对方确认。接收方一般采用累计确认，对按序到达的最后一个分组发送确认，表明到这个分组为止的所有分组都已经正确收到了。

**优点：** 信道利用率高，容易实现，即使确认丢失，也不必重传。

**缺点：** 不能向发送方反映出接收方已经正确收到的所有分组的信息。 比如：发送方发送了 5条 消息，中间第三条丢失（3号），这时接收方只能对前两个发送确认。发送方无法知道后三个分组的下落，而只好把后三个全部重传一次。这也叫 Go-Back-N（回退 N），表示需要退回来重传已经发送过的 N 个消息。

### 9.滑动窗口和流量控制

**TCP 利用滑动窗口实现流量控制。流量控制是为了控制发送方发送速率，保证接收方来得及接收。** 接收方发送的确认报文中的窗口字段可以用来控制发送方窗口大小，从而影响发送方的发送速率。将窗口字段设置为 0，则发送方不能发送数据。

### 10.拥塞控制

在某段时间，若对网络中某一资源的需求超过了该资源所能提供的可用部分，网络的性能就要变坏。这种情况就叫拥塞。拥塞控制就是为了防止过多的数据注入到网络中，这样就可以使网络中的路由器或链路不致过载。拥塞控制所要做的都有一个前提，就是网络能够承受现有的网络负荷。拥塞控制是一个全局性的过程，涉及到所有的主机，所有的路由器，以及与降低网络传输性能有关的所有因素。相反，流量控制往往是点对点通信量的控制，是个端到端的问题。流量控制所要做到的就是抑制发送端发送数据的速率，以便使接收端来得及接收。

为了进行拥塞控制，TCP 发送方要维持一个 **拥塞窗口(cwnd)** 的状态变量。拥塞控制窗口的大小取决于网络的拥塞程度，并且动态变化。发送方让自己的发送窗口取为拥塞窗口和接收方的接受窗口中较小的一个。

TCP的拥塞控制采用了四种算法，即 **慢开始** 、 **拥塞避免** 、**快重传** 和 **快恢复**。在网络层也可以使路由器采用适当的分组丢弃策略（如主动队列管理 AQM），以减少网络拥塞的发生。

- **慢开始：** 慢开始算法的思路是当主机开始发送数据时，如果立即把大量数据字节注入到网络，那么可能会引起网络阻塞，因为现在还不知道网络的符合情况。经验表明，较好的方法是先探测一下，即由小到大逐渐增大发送窗口，也就是由小到大逐渐增大拥塞窗口数值。cwnd初始值为1，每经过一个传播轮次，cwnd加倍。
- **拥塞避免：** 拥塞避免算法的思路是让拥塞窗口cwnd缓慢增大，即每经过一个往返时间RTT就把发送放的cwnd加1.
- **快重传与快恢复：** **在 TCP/IP 中，快速重传和恢复（fast retransmit and recovery，FRR）是一种拥塞控制算法，它能快速恢复丢失的数据包。**没有 快速重传和恢复，如果数据包丢失了，TCP 将会使用定时器来要求传输暂停。在暂停的这段时间内，没有新的或复制的数据包被发送。有了 快速重传和恢复，**如果接收方接收到一个不按顺序的数据段，它会立即给发送方发送一个重复确认。如果发送方接收到三个重复确认，它会认为数据段丢失了，并立即重传这些丢失的数据段。**有了 FRR，就不会因为重传时要求的暂停被耽误。 　当有单独的数据包丢失时，快速重传和恢复（FRR）能最有效地工作。当有多个数据信息包在某一段很短的时间内丢失时，它则不能很有效地工作。

### 11.从输入URL到页面加载经历了什么

**总体来说分为以下几个过程:**

1. **浏览器的地址栏输入URL并按下回车**
2. **浏览器查找当前URL是否存在缓存，并比较缓存是否过期**
3. **DNS解析URL对应的IP**
4. **TCP连接（三次握手）**
5. **发送HTTP请求**
6. **服务器处理请求并返回HTTP报文**
7. **浏览器解析渲染页面**
8. **TCP连接结束（四次挥手）**

**详细过程如下**：

1. **浏览器的地址栏输入URL并按下回车**

   我们常见的URL是这样的:[http://www.baidu.com](http://www.baidu.com/)，这个域名由三部分组成：协议名、域名、端口号，这里端口是默认所以隐藏。除此之外URL还会包含一些路径、查询或其他片段，例如：http://www.tuicool.com/search?kw=%E4%。我们最常见的的协议是HTTP协议，除此之外还有加密的HTTPS协议、FTP协议等等。URL的中间部分为域名或者是IP，之后是端口号。通常端口号不常见是因为大部分都是使用默认端口，如HTTP默认端口80，HTTPS默认端口443。

2. **浏览器查找当前URL是否存在缓存，并比较缓存是否过期**

   HTTP缓存有多种规则，根据是否需要向服务器重新发起请求来分类，将其分为强制缓存，对比缓存。

   **强制缓存：**判断HTTP首部字段：**Expires 和 cache-control**。

   Expires是一个绝对时间，即服务器时间。浏览器检查当前时间，如果还没到失效时间就直接使用缓存文件。但是该方法存在一个问题：服务器时间与客户端时间可能不一致。

   cache-control中的max-age保存了一个相对时间。例如Cache-Control: max-age = 484200，表示浏览器收到文件后，缓存在484200s内有效。 如果同时存在cache-control和Expires，浏览器总是优先使用cache-control。

   **对比缓存：**通过HTTP的 last-modified（最新-改进），Etag字段进行判断。

   **last-modified 表示请求的URL（资源）最后一次更新的时间。**下一次浏览器请求资源时就发送if-modified-since字段。服务器用本地Last-modified时间与if-modified-since时间比较，如果不一致则认为缓存已过期并返回新资源给浏览器；如果时间一致则发送304状态码，让浏览器继续使用缓存。

   **Etag：资源的实体标识（哈希字符串），当资源内容更新时，Etag会改变。**服务器会判断Etag是否发生变化，如果变化则返回新资源，否则返回304。

3. **DNS解析**

   **DNS解析的过程就是寻找哪台机器上有你需要资源的过程**。当你在浏览器中输入一个地址时，例如www.baidu.com，其实不是百度网站真正意义上的地址。互联网上每一台计算机的唯一标识是它的IP地址，但是IP地址并不方便记忆。用户更喜欢用方便记忆的网址去寻找互联网上的其它计算机。

   ![DNS解析](/Users/zhangyi/Desktop/Java复习图/DNS解析.png)

   **DNS解析是一个递归查询的过程：**

   1. **首先浏览器先检查本地hosts文件是否有这个网址映射关系，如果有就调用这个IP地址映射，完成域名解析。**
   2. **hosts文件没有则在本地域名服务器中查询IP地址，如果没有找到的情况下，本地域名服务器会向根域名服务器发送一个请求**
   3. **如果根域名服务器也不存在该域名时，本地域名会向com顶级域名服务器发送一个请求**
   4. **依次类推下去。直到最后本地域名服务器得到google的IP地址并把它缓存到本地，供下次查询使用**

   **DNS缓存：**

   DNS存在着多级缓存，从离浏览器的距离排序的话，有以下几种: **浏览器缓存，系统缓存，路由器缓存，IPS服务器缓存，根域名服务器缓存，顶级域名服务器缓存，主域名服务器缓存。**

4. **TCP连接**

5. **发送HTTP请求**

6. **服务器处理请求并返回HTTP报文**

7. **浏览器解析渲染页面，渲染成DOM树**

8. **连接结束**

### 12.状态码

![状态码](/Users/zhangyi/Desktop/Java复习图/状态码.png)

**1开头：（信息提示**

- 100 continue 该状态码说明服务器收到了请求的初始部分，并且请客户端继续发送。

**2开头：（请求成功）表示成功处理了请求的状态代码**

- 200：（OK/成功）服务器已成功处理了请求。通常，这表示服务器提供了请求的网页。
- 201：（Created/已创建）请求成功并且服务器创建了新的资源
- 202：（Accepted/已接受）服务器已接受请求，但尚未处理
- 203：（Non-Authoritative Information/非授权信息）服务器已成功处理了请求，但返回的信息可能来自另一资源。
- 204：（No Content/无内容）服务器成功处理了请求，但没有返回任何内容
- 205：（Reset Content/重置内容）服务器成功处理了请求，但没有返回任何内容
- 206：（Partial Content/部分内容）服务器成功处理了部分 GET 请求

**3开头：（请求被重定向）表示要完成请求，需要进一步操作。通常，这些状态代码用来重定向**

- 300：（Multiple Choices/多种选择）针对请求，服务器可执行多种操作。服务器可根据请求者（user agent）选择一项操作，或提供操作列表供请求者选择
- 301：（Moved Permanently/永久移动）请求的网页已永久移动到新位置。服务器返回此响应（对 GET 或 HEAD 请求的响应）时，会自动将请求者转到新的位置
- 302：（Found/临时移动）服务器目前从不同位置的网页响应请求，但请求者应继续使用原有位置来进行以后的请求
- 303：（See Other/参见其他信息）请求者应当对不同的位置使用单独的 GET 请求来检索响应时，服务器返回此代码
- 304：（Not Modified/未修改）自从上次请求后，请求的网页未修改过。服务器返回此响应，不会返回网页内容
- 305：（Use Proxy/使用代理）请求者只能使用代理访问请求的网页。如果服务器返回此响应，还表示请求者应使用代理
- 307：（Temporary Redirect/临时重定向）服务器目前从不同位置的网页响应请求，但请求者继续使用原有位置来进行以后的请求

**4开头：（请求错误）这些状态码表示请求可能出错，妨碍了服务器的处理**

- 400：（Bad Request/错误请求）服务器不理解请求的语法
- 401：（Unauthorized/未授权）请求要求身份验证。对于需要登录的网页，服务器可能返回此响应
- 403：（Forbidden/禁止）服务器拒绝请求
- 404：（Not Found/未找到）服务器找不到请求的网页
- 405：（Method Not Allowed/方法禁用）禁用请求中指定的方法
- 406：（Not Acceptable/不接受）无法使用请求的内容特性响应请求的网页
- 407：（Proxy Authentication Required/需要代理授权）此状态代码与 401 （未授权）类似，但指定请求者应当授权使用代理
- 408：（Request Timeout/请求超时）服务器等候请求时发生超时
- 409：（Conflict/冲突）服务器在完成请求时发生冲突。服务器必须在响应中包含有关冲突的信息
- 410：（Gone/已删除）如果请求的资源已永久删除，服务器就会返回此响应

**5开头：（服务器错误）这些状态代码表示服务器在尝试处理请求时发生内部错误。这些错误可能是服务器本身的错误，而不是请求出错。**

- 500：（Internal Server Error/服务器内部错误）服务器遇到错误，无法完成请求
- 501：（Not Implemented/尚未实现）服务器不具备完成请求的功能。例如，服务器无法识别请求方法时可能会返回此代码
- 502：（Bad Gateway/错误网关）服务器作为网关或代理，从上游服务器收到无效响应
- 503：（Service Unavailable/服务不可用）服务器目前无法使用（由于超载或停机维护）。通常，这只是暂时状态
- 504：（Gateway Timeout/网关超时）服务器作为网关或代理，但是没有及时从上游服务器收到请求
- 505：（HTTP Version Not Supported/HTTP 版本不受支持）服务器不支持请求中所用的 HTTP 协议版本

### 13.各种协议与HTTP关系

![各种协议与HTTP协议之间的关系](/Users/zhangyi/Desktop/Java复习图/各种协议与HTTP协议之间的关系.png)

### 14.HTTP长连接，短连接

在HTTP/1.0中默认使用短连接。也就是说，客户端和服务器每进行一次HTTP操作，就建立一次连接，任务结束就中断连接。当客户端浏览器访问的某个HTML或其他类型的Web页中包含有其他的Web资源（如JavaScript文件、图像文件、CSS文件等），每遇到这样一个Web资源，浏览器就会重新建立一个HTTP会话。

而从HTTP/1.1起，默认使用长连接，用以保持连接特性。使用长连接的HTTP协议，会在响应头加入这行代码：

```
Connection:keep-alive
```

在使用长连接的情况下，当一个网页打开完成后，客户端和服务器之间用于传输HTTP数据的TCP连接不会关闭，客户端再次访问这个服务器时，会继续使用这一条已经建立的连接。Keep-Alive不会永久保持连接，它有一个保持时间，可以在不同的服务器软件（如Apache）中设定这个时间。实现长连接需要客户端和服务端都支持长连接。

**HTTP协议的长连接和短连接，实质上是TCP协议的长连接和短连接。**

### 15.HTTP是不保存状态的协议，如何保存用户状态

HTTP 是一种不保存状态，即无状态（stateless）协议。也就是说 HTTP 协议自身不对请求和响应之间的通信状态进行保存。那么我们保存用户状态呢？Session 机制的存在就是为了解决这个问题，Session 的主要作用就是通过服务端记录用户的状态。典型的场景是购物车，当你要添加商品到购物车的时候，系统不知道是哪个用户操作的，因为 HTTP 协议是无状态的。服务端给特定的用户创建特定的 Session 之后就可以标识这个用户并且跟踪这个用户了（一般情况下，服务器会在一定时间内保存这个 Session，过了时间限制，就会销毁这个Session）。

在服务端保存 Session 的方法很多，最常用的就是内存和数据库(比如是使用内存数据库redis保存)。既然 Session 存放在服务器端，那么我们如何实现 Session 跟踪呢？大部分情况下，我们都是通过在 Cookie 中附加一个 Session ID 来方式来跟踪。

**Cookie 被禁用怎么办?**

最常用的就是利用 URL 重写把 Session ID 直接附加在URL路径的后面。

### 16.cookie的作用是什么？和session有什么区别

Cookie 和 Session都是用来跟踪浏览器用户身份的会话方式，但是两者的应用场景不太一样。

**Cookie 一般用来保存用户信息** 比如①我们在 Cookie 中保存已经登录过得用户信息，下次访问网站的时候页面可以自动帮你登录的一些基本信息给填了；②一般的网站都会有保持登录也就是说下次你再访问网站的时候就不需要重新登录了，这是因为用户登录的时候我们可以存放了一个 Token 在 Cookie 中，下次登录的时候只需要根据 Token 值来查找用户即可(为了安全考虑，重新登录一般要将 Token 重写)；③登录一次网站后访问网站其他页面不需要重新登录。

**Session 的主要作用就是通过服务端记录用户的状态。** 典型的场景是购物车，当你要添加商品到购物车的时候，系统不知道是哪个用户操作的，因为 HTTP 协议是无状态的。服务端给特定的用户创建特定的 Session 之后就可以标识这个用户并且跟踪这个用户了。

Cookie 数据保存在客户端(浏览器端)，Session 数据保存在服务器端。

**Cookie 存储在客户端中，而Session存储在服务器上，相对来说 Session 安全性更高。如果要在 Cookie 中存储一些敏感信息，不要直接写入 Cookie 中，最好能将 Cookie 信息加密然后使用到的时候再去服务器端解密。**

### 17.HTTP1.0和1.1的区别

1. **长连接** : **在HTTP/1.0中，默认使用的是短连接**，也就是说每次请求都要重新建立一次连接。HTTP 是基于TCP/IP协议的,每一次建立或者断开连接都需要三次握手四次挥手的开销，如果每次请求都要这样的话，开销会比较大。因此最好能维持一个长连接，可以用个长连接来发多个请求。**HTTP 1.1起，默认使用长连接** ,默认开启Connection： keep-alive。 **HTTP/1.1的持续连接有非流水线方式和流水线方式** 。流水线方式是客户在收到HTTP的响应报文之前就能接着发送新的请求报文。与之相对应的非流水线方式是客户在收到前一个响应后才能发送下一个请求。
2. **错误状态响应码** :在HTTP1.1中新增了24个错误状态响应码，如409（Conflict）表示请求的资源与资源的当前状态发生冲突；410（Gone）表示服务器上的某个资源被永久性的删除。
3. **缓存处理** :在HTTP1.0中主要使用header里的If-Modified-Since,Expires来做为缓存判断的标准，HTTP1.1则引入了更多的缓存控制策略例如Entity tag，If-Unmodified-Since, If-Match, If-None-Match等更多可供选择的缓存头来控制缓存策略。
4. **带宽优化及网络连接的使用** :HTTP1.0中，存在一些浪费带宽的现象，例如客户端只是需要某个对象的一部分，而服务器却将整个对象送过来了，并且不支持断点续传功能，HTTP1.1则在请求头引入了range头域，它允许只请求资源的某个部分，即返回码是206（Partial Content），这样就方便了开发者自由的选择以便于充分利用带宽和连接。

### 18.URL和URI

- URI(Uniform Resource Identifier) 是统一资源标志符，可以唯一标识一个资源。
- URL(Uniform Resource Location) 是统一资源定位符，可以提供该资源的路径。它是一种具体的 URI，即 URL 可以用来标识一个资源，而且还指明了如何 locate 这个资源。

URI的作用像身份证号一样，URL的作用更像家庭住址一样。URL是一种具体的URI，它不仅唯一标识资源，而且还提供了定位该资源的信息。

### 19.HTTP和HTTPS

1. **端口** ：HTTP的URL由“http://”起始且默认使用端口80，而HTTPS的URL由“https://”起始且默认使用端口443。

2. 安全性和资源消耗：

   HTTP协议运行在TCP之上，所有传输的内容都是明文，客户端和服务器端都无法验证对方的身份。HTTPS是运行在SSL/TLS之上的HTTP协议，SSL/TLS 运行在TCP之上。**所有传输的内容都经过加密，加密采用对称加密，但对称加密的密钥用服务器方的证书进行了非对称加密。**所以说，HTTP 安全性没有 HTTPS高，但是 HTTPS 比HTTP耗费更多服务器资源。

   - 对称加密：**密钥只有一个，加密解密为同一个密码，且加解密速度快，典型的对称加密算法有DES、AES等；**
   - 非对称加密：**密钥成对出现（且根据公钥无法推知私钥，根据私钥也无法推知公钥），加密解密使用不同密钥（公钥加密需要私钥解密，私钥加密需要公钥解密），相对对称加密速度较慢，典型的非对称加密算法有RSA、DSA等。**

   



# 操作系统

### 1.进程、线程、协程

1. **进程**

   **进程是对运行时程序的封装，是系统进行资源调度和分配的基本单位，实现了OS的并发。**

   进程是程序执行一次的过程，是系统运行程序的基本单位，是动态的。

2. **线程**

   **线程是进程的子任务，是CPU调度和分派的基本单位。**

   线程是CPU运行的基本单位，线程包含在进程中，一个进程运行可以产生多个线程。

3. **协程**

   **协程是一种用户态的轻量级线程，没有增加线程的数量，只是在线程的基础上通过分时服用的方式运行多个协程。**

   **线程的问题：一是系统线程会占用非常多的内存空间，二是过多的线程切换会占用大量的系统时间。**

   **协程的切换在用户态完成，切换的代价比线程从用户态到内核态的代价小很多**

   协程的调度完全由用户控制。协程拥有自己的寄存器上下文和栈。协程调度切换时，将寄存器上下文和栈保存到其他地方，在切回来的时候，恢复先前保存的寄存器上下文和栈，直接操作栈则基本没有内核切换的开销，可以不加锁的访问全局变量，所以上下文的切换非常快。

   

- **一个程序运行至少一个进程，一个进程至少一个线程，一个线程可以多个协程**
- **线程、进程都是同步机制，而协程则是异步**
- **协程能保留上一次调用时的状态，每次过程重入时，就相当于进入上一次调用的状态**

### 2.用户态和和心态

1. **用户态(user mode) : 用户态运行的进程或可以直接读取用户程序的数据。**
2. **系统态(kernel mode):可以简单的理解系统态运行的进程或程序几乎可以访问计算机的任何资源，不受限制。**

在用户程序中，凡是与系统态级别的资源有关的操作（如文件管理、进程控制、内存管理等)，都必须通过系统调用方式向操作系统提出服务请求，并由操作系统代为完成。

这些系统调用按功能大致可分为如下几类：

- 设备管理。完成设备的请求或释放，以及设备启动等功能。
- 文件管理。完成文件的读、写、创建及删除等功能。
- 进程控制。完成进程的创建、撤销、阻塞及唤醒等功能。
- 进程通信。完成进程之间的消息传递或信号传递等功能。
- 内存管理。完成内存的分配、回收以及获取作业占用内存区大小及地址等功能。

### 3.进程间通信的方式

1. **管道/匿名管道(Pipes)** ：用于具有亲缘关系的父子进程间或者兄弟进程之间的通信。

   - 管道是半双工的，数据只能向一个方向流动；需要双方通信时，需要建立起两个管道。
   - 只能用于父子进程或者兄弟进程之间(具有亲缘关系的进程);
   - 单独构成一种独立的文件系统：管道对于管道两端的进程而言，就是一个文件，但它不是普通的文件，它不属于某种文件系统，而是自立门户，单独构成一种文件系统，并且只存在与内存中。
   - 数据的读出和写入：一个进程向管道中写的内容被管道另一端的进程读出。写入的内容每次添加在管道缓冲区的末尾，并且每次都从缓冲区的头部读出数据。

   **管道的实质：**
   管道的实质是一个内核缓冲区，进程以先进先出的方式从缓冲区存取数据，管道一端的进程顺序的将数据写入缓冲区，另一端的进程则顺序的读出数据。

   该缓冲区可以看做是一个循环队列，读和写的位置都是自动增长的，不能随意改变，一个数据只能被读一次，读出来以后在缓冲区就不复存在了。

   当缓冲区读空或者写满时，有一定的规则控制相应的读进程或者写进程进入等待队列，当空的缓冲区有新数据写入或者满的缓冲区有数据读出来时，就唤醒等待队列中的进程继续读写。

   **管道的局限：**
   管道的主要局限性正体现在它的特点上：

   - 只支持单向数据流；
   - 只能用于具有亲缘关系的进程之间；
   - 没有名字；
   - 管道的缓冲区是有限的（管道制存在于内存中，在管道创建时，为缓冲区分配一个页面大小）
   - 管道所传送的是无格式字节流，这就要求管道的读出方和写入方必须事先约定好数据的格式，比如多少字节算作一个消息（或命令、或记录）等等；

2. **有名管道(Names Pipes)** : 匿名管道由于没有名字，只能用于亲缘关系的进程间通信。为了克服这个缺点，提出了有名管道。有名管道严格遵循**先进先出(first in first out)**。有名管道以磁盘文件的方式存在，可以实现本机任意两个进程通信。

3. **信号(Signal)** ：信号是一种比较复杂的通信方式，用于通知接收进程某个事件已经发生；

4. **消息队列(Message Queuing)** ：消息队列是消息的链表,具有特定的格式,存放在内存中并由消息队列标识符标识。管道和消息队列的通信数据都是先进先出的原则。与管道（无名管道：只存在于内存中的文件；命名管道：存在于实际的磁盘介质或者文件系统）不同的是消息队列存放在内核中，只有在内核重启(即，操作系统重启)或者显示地删除一个消息队列时，该消息队列才会被真正的删除。消息队列可以实现消息的随机查询,消息不一定要以先进先出的次序读取,也可以按消息的类型读取.比 FIFO 更有优势。**消息队列克服了信号承载信息量少，管道只能承载无格式字 节流以及缓冲区大小受限等缺。**

5. **信号量(Semaphores)** ：信号量是一个计数器，用于多进程对共享数据的访问，信号量的意图在于进程间同步。这种通信方式主要用于解决与同步相关的问题并避免竞争条件。

6. **共享内存(Shared memory)** ：使得多个进程可以访问同一块内存空间，不同进程可以及时看到对方进程中对共享内存中数据的更新。这种方式需要依靠某种同步操作，如互斥锁和信号量等。可以说这是最有用的进程间通信方式。

7. **套接字(Sockets)** : 此方法主要用于在客户端和服务器之间通过网络进行通信。套接字是支持 TCP/IP 的网络通信的基本操作单元，可以看做是不同主机之间的进程进行双向通信的端点，简单的说就是通信的两方的一种约定，用套接字中的相关函数来完成通信过程。

### **4.线程间同步的方式**

1. **互斥量(Mutex)**：采用互斥对象机制，只有拥有互斥对象的线程才有访问公共资源的权限。因为互斥对象只有一个，所以可以保证公共资源不会被多个线程同时访问。比如 Java 中的 synchronized 关键词和各种 Lock 都是这种机制。
2. **信号量(Semphares)** ：它允许同一时刻多个线程访问同一资源，但是需要控制同一时刻访问此资源的最大线程数量
3. **事件(Event)** :Wait/Notify：通过通知操作的方式来保持多线程同步，还可以方便的实现多线程优先级的比较操

### 5.进程调度算法

- **先到先服务(FCFS)调度算法** : 从就绪队列中选择一个最先进入该队列的进程为之分配资源，使它立即执行并一直执行到完成或发生某事件而被阻塞放弃占用 CPU 时再重新调度。
- **短作业优先(SJF)的调度算法** : 从就绪队列中选出一个估计运行时间最短的进程为之分配资源，使它立即执行并一直执行到完成或发生某事件而被阻塞放弃占用 CPU 时再重新调度。
- **时间片轮转调度算法** : 时间片轮转调度是一种最古老，最简单，最公平且使用最广的算法，又称 RR(Round robin)调度。每个进程被分配一个时间段，称作它的时间片，即该进程允许运行的时间。
- **多级反馈队列调度算法** ：前面介绍的几种进程调度的算法都有一定的局限性。如**短进程优先的调度算法，仅照顾了短进程而忽略了长进程** 。多级反馈队列调度算法既能使高优先级的作业得到响应又能使短作业（进程）迅速完成。，因而它是目前**被公认的一种较好的进程调度算法**，UNIX 操作系统采取的便是这种调度算法。
- **优先级调度** ： 为每个流程分配优先级，首先执行具有最高优先级的进程，依此类推。具有相同优先级的进程以 FCFS 方式执行。可以根据内存要求，时间要求或任何其他资源要求来确定优先级。

### 6.操作系统内存管理介绍

操作系统的内存管理主要负责**内存的分配与回收**（malloc 函数：申请内存，free 函数：释放内存），另外地址转换也就是**将逻辑地址转换成相应的物理地址**等功能也是操作系统内存管理做的事情。

### 7.内存管理机制

简单分为**连续分配管理方式**和**非连续分配管理方式**这两种。连续分配管理方式是指为一个用户程序分配一个连续的内存空间，常见的如 **块式管理** 。同样地，非连续分配管理方式允许一个程序使用的内存分布在离散或者说不相邻的内存中，常见的如**页式管理** 和 **段式管理**。

1. **块式管理** ： 远古时代的计算机操系统的内存管理方式。将内存分为几个固定大小的块，每个块中只包含一个进程。如果程序运行需要内存的话，操作系统就分配给它一块，如果程序运行只需要很小的空间的话，分配的这块内存很大一部分几乎被浪费了。这些在每个块中未被利用的空间，我们称之为碎片。
2. **页式管理** ：把主存分为大小相等且固定的一页一页的形式，页较小，相对相比于块式管理的划分力度更大，提高了内存利用率，减少了碎片。页式管理通过页表对应逻辑地址和物理地址。
3. **段式管理** ： 页式管理虽然提高了内存利用率，但是页式管理其中的页实际并无任何实际意义。 段式管理把主存分为一段段的，每一段的空间又要比一页的空间小很多 。但是，最重要的是段是有实际意义的，每个段定义了一组逻辑信息，例如,有主程序段 MAIN、子程序段 X、数据段 D 及栈段 S 等。 段式管理通过段表对应逻辑地址和物理地址。
4. **段页式管理机制** ：段页式管理机制结合了段式管理和页式管理的优点。简单来说段页式管理机制就是把主存先分成若干段，每个段又分成若干页，也就是说 **段页式管理机制** 中段与段之间以及段的内部的都是离散的。

### 8.快表和多级页表

在分页内存管理中，很重要的两点是：

1. **虚拟地址到物理地址的转换要快。**
2. **解决虚拟地址空间大，页表也会很大的问题。**

**快表：**

为了解决虚拟地址到物理地址的转换速度，操作系统在 **页表方案** 基础之上引入了 **快表** 来加速虚拟地址到物理地址的转换。我们可以把快表理解为一种**特殊的高速缓冲存储器（Cache）**，其中的内容是页表的一部分或者全部内容。作为页表的 Cache，它的作用与页表相似，但是提高了访问速率。由于采用页表做地址转换，读写内存数据时 CPU 要访问两次主存。有了快表，有时只要访问一次高速缓冲存储器(Cache)，一次主存，这样可加速查找并提高指令执行速度。

使用快表之后的地址转换流程是这样的：

1. 根据虚拟地址中的页号查快表；
2. 如果该页在快表中，直接从快表中读取相应的物理地址；
3. 如果该页不在快表中，就访问内存中的页表，再从页表中得到物理地址，同时将页表中的该映射表项添加到快表中；
4. 当快表填满后，又要登记新页时，就按照一定的淘汰策略淘汰掉快表中的一个页。

看完了之后你会发现快表和我们平时经常在我们开发的系统使用的缓存（比如 Redis）很像，的确是这样的，操作系统中的很多思想、很多经典的算法，你都可以在我们日常开发使用的各种工具或者框架中找到它们的影子。

**多级页表**

引入多级页表的主要目的是为了避免把全部页表一直放在内存中占用过多空间，特别是那些根本就不需要的页表就不需要保留在内存中。多级页表属于时间换空间的典型场景，具体可以查看下面这篇文章

- 多级页表如何节约内存：https://www.polarxiong.com/archives/多级页表如何节约内存.html

**总结**

为了提高内存的空间性能，提出了多级页表的概念；但是提到空间性能是以浪费时间性能为基础的，因此为了补充损失的时间性能，提出了快表（即 TLB）的概念。 不论是快表还是多级页表实际上都利用到了程序的局部性原理，局部性原理在后面的虚拟内存这部分会介绍到。

### 9.分页机制和分段机制的共同和区别

1. 共同点：
   - 分页机制和分段机制都是为了提高内存利用率，减少内存碎片。
   - 页和段都是离散存储的，所以两者都是离散分配内存的方式。但是，每个页和段中的内存是连续的。
2. 区别：
   - 页的大小是固定的，由操作系统决定；而段的大小不固定，取决于我们当前运行的程序。
   - 分页仅仅是为了满足操作系统内存管理的需求，而段是逻辑信息的单位，在程序中可以体现为代码段，数据段，能够更好满足用户的需要。

### 10.虚拟(逻辑)地址和物理地址



我们编程一般只有可能和逻辑地址打交道，比如在 **C 语言中，指针里面存储的数值就可以理解成为内存里的一个地址，这个地址也就是我们说的逻辑地址，逻辑地址由操作系统决定。物理地址指的是真实物理内存中地址，更具体一点来说就是内存地址寄存器中的地址。物理地址是内存单元真正的地址。**

### 11.CPU寻址，为何使用虚拟地址空间

现代处理器使用的是一种称为 **虚拟寻址(Virtual Addressing)** 的寻址方式。**使用虚拟寻址，CPU 需要将虚拟地址翻译成物理地址，这样才能访问到真实的物理内存。** 实际上完成虚拟地址转换为物理地址转换的硬件是 CPU 中含有一个被称为 **内存管理单元（Memory Management Unit, MMU）** 的硬件。如下图所示：

![虚拟地址](/Users/zhangyi/Desktop/Java复习图/虚拟地址.png)

**为什么要有虚拟地址空间呢？**

先从没有虚拟地址空间的时候说起吧！没有虚拟地址空间的时候，**程序都是直接访问和操作的都是物理内存** 。但是这样有什么问题呢？

1. 用户程序可以访问任意内存，寻址内存的每个字节，这样就很容易（有意或者无意）破坏操作系统，造成操作系统崩溃。
2. 想要同时运行多个程序特别困难，比如你想同时运行一个微信和一个 QQ 音乐都不行。为什么呢？举个简单的例子：微信在运行的时候给内存地址 1xxx 赋值后，QQ 音乐也同样给内存地址 1xxx 赋值，那么 QQ 音乐对内存的赋值就会覆盖微信之前所赋的值，这就造成了微信这个程序就会崩溃。

通过虚拟地址访问内存有以下优势：

- 程序可以使用一系列相邻的虚拟地址来访问物理内存中不相邻的大内存缓冲区。
- 程序可以使用一系列虚拟地址来访问大于可用物理内存的内存缓冲区。当物理内存的供应量变小时，内存管理器会将物理内存页（通常大小为 4 KB）保存到磁盘文件。数据或代码页会根据需要在物理内存与磁盘之间移动。
- 不同进程使用的虚拟地址彼此隔离。一个进程中的代码无法更改正在由另一进程或操作系统使用的物理内存。

### 12.虚拟内存

通过 **虚拟内存** 可以让程序可以拥有超过系统物理内存大小的可用内存空间。另外，**虚拟内存为每个进程提供了一个一致的、私有的地址空间，它让每个进程产生了一种自己在独享主存的错觉（每个进程拥有一片连续完整的内存空间）**。这样会更加有效地管理内存并减少出错。

**虚拟内存**是计算机系统内存管理的一种技术，我们可以手动设置自己电脑的虚拟内存。不要单纯认为虚拟内存只是“使用硬盘空间来扩展内存“的技术。**虚拟内存的重要意义是它定义了一个连续的虚拟地址空间**，并且 **把内存扩展到硬盘空间**。推荐阅读：[《虚拟内存的那点事儿》](https://juejin.im/post/59f8691b51882534af254317)

维基百科中有几句话是这样介绍虚拟内存的。

> **虚拟内存** 使得应用程序认为它拥有连续的可用的内存（一个连续完整的地址空间），而实际上，它通常是被分隔成多个物理内存碎片，还有部分暂时存储在外部磁盘存储器上，在需要时进行数据交换。与没有使用虚拟内存技术的系统相比，使用这种技术的系统使得大型程序的编写变得更容易，对真正的物理内存（例如 RAM）的使用也更有效率。目前，大多数操作系统都使用了虚拟内存，如 Windows 家族的“虚拟内存”；Linux 的“交换空间”等。

### 13.局部性原理

局部性原理是虚拟内存技术的基础，正是因为程序运行具有局部性原理，才可以只装入部分程序到内存就开始运行。

**程序在执行的时候往往呈现局部性规律，也就是说在某个较短的时间段内，程序执行局限于某一小部分，程序访问的存储空间也局限于某个区域。**

局部性原理表现在以下两个方面：

1. **时间局部性** ：如果程序中的某条指令一旦执行，不久以后该指令可能再次执行；如果某数据被访问过，不久以后该数据可能再次被访问。产生时间局部性的典型原因，是由于在程序中存在着大量的循环操作。
2. **空间局部性** ：一旦程序访问了某个存储单元，在不久之后，其附近的存储单元也将被访问，即程序在一段时间内所访问的地址，可能集中在一定的范围之内，这是因为指令通常是顺序存放、顺序执行的，数据也一般是以向量、数组、表等形式簇聚存储的。

时间局部性是通过将近来使用的指令和数据保存到Cache(高速缓存存储器)中，并使用高速缓存的层次结构实现。空间局部性通常是使用较大的高速缓存，并将预取机制集成到Cache(高速缓存)控制逻辑中实现。虚拟内存技术实际上就是建立了 “内存一外存”的两级存储器的结构，利用局部性原理实现髙速缓存。

### 14.虚拟内存(虚拟存储器)

**虚拟存储器又叫做虚拟内存，都是 Virtual Memory 的翻译，属于同一个概念。**

基于局部性原理，在程序装入时，可以将程序的一部分装入内存，而将其他部分留在外存，就可以启动程序执行。由于外存往往比内存大很多，所以我们运行的软件的内存大小实际上是可以比计算机系统实际的内存大小大的。在程序执行过程中，当所访问的信息不在内存时，由操作系统将所需要的部分调入内存，然后继续执行程序。另一方面，操作系统将内存中暂时不使用的内容换到外存上，从而腾出空间存放将要调入内存的信息。这样，计算机好像为用户提供了一个比实际内存大的多的存储器——**虚拟存储器**。

实际上，我觉得虚拟内存同样是一种时间换空间的策略，你用 CPU 的计算时间，页的调入调出花费的时间，换来了一个虚拟的更大的空间来支持程序的运行。不得不感叹，程序世界几乎不是时间换空间就是空间换时间。

### 15.虚拟内存的技术实现

**虚拟内存的实现需要建立在离散分配的内存管理方式的基础上。** 虚拟内存的实现有以下三种方式：

1. **请求分页存储管理** ：建立在分页管理之上，为了支持虚拟存储器功能而增加了请求调页功能和页面置换功能。请求分页是目前最常用的一种实现虚拟存储器的方法。请求分页存储管理系统中，在作业开始运行之前，仅装入当前要执行的部分段即可运行。假如在作业运行的过程中发现要访问的页面不在内存，则由处理器通知操作系统按照对应的页面置换算法将相应的页面调入到主存，同时操作系统也可以将暂时不用的页面置换到外存中。
2. **请求分段存储管理** ：建立在分段存储管理之上，增加了请求调段功能、分段置换功能。请求分段储存管理方式就如同请求分页储存管理方式一样，在作业开始运行之前，仅装入当前要执行的部分段即可运行；在执行过程中，可使用请求调入中断动态装入要访问但又不在内存的程序段；当内存空间已满，而又需要装入新的段时，根据置换功能适当调出某个段，以便腾出空间而装入新的段。
3. **请求段页式存储管理**

**分页与分页存储管理，两者有何不同呢？**

请求分页存储管理建立在分页管理之上。他们的根本区别是是否将程序全部所需的全部地址空间都装入主存，这也是请求分页存储管理可以提供虚拟内存的原因，我们在上面已经分析过了。

它们之间的根本区别在于是否将一作业的全部地址空间同时装入主存。请求分页存储管理不要求将作业全部地址空间同时装入主存。基于这一点，请求分页存储管理可以提供虚存，而分页存储管理却不能提供虚存。

不管是上面那种实现方式，我们一般都需要：

1. **一定容量的内存和外存**：在载入程序的时候，只需要将程序的一部分装入内存，而将其他部分留在外存，然后程序就可以执行了；
2. **缺页中断**：如果**需执行的指令或访问的数据尚未在内存**（称为缺页或缺段），则由处理器通知操作系统将相应的页面或段**调入到内存**，然后继续执行程序；
3. **虚拟地址空间** ：逻辑地址到物理地址的变换。

### 16.页面置换算法

**地址映射过程中，若在页面中发现所要访问的页面不在内存中，则发生缺页中断 。**

> **缺页中断** 就是要访问的**页**不在主存，需要操作系统将其调入主存后再进行访问。 在这个时候，被内存映射的文件实际上成了一个分页交换文件。

当发生缺页中断时，如果当前内存中并没有空闲的页面，操作系统就必须在内存选择一个页面将其移出内存，以便为即将调入的页面让出空间**。用来选择淘汰哪一页的规则叫做页面置换算法，我们可以把页面置换算法看成是淘汰页面的规则。**

- **OPT 页面置换算法（最佳页面置换算法）** ：最佳(Optimal, OPT)置换算法所选择的被淘汰页面将是以后永不使用的，或者是在最长时间内不再被访问的页面,这样可以保证获得最低的缺页率。但由于人们目前无法预知进程在内存下的若千页面中哪个是未来最长时间内不再被访问的，因而该算法无法实现。一般作为衡量其他置换算法的方法。
- **FIFO（First In First Out） 页面置换算法（先进先出页面置换算法）** : 总是淘汰最先进入内存的页面，即选择在内存中驻留时间最久的页面进行淘汰。
- **LRU （Least Currently Used）页面置换算法（最近最久未使用页面置换算法）** ：LRU算法赋予每个页面一个访问字段，用来记录一个页面自上次被访问以来所经历的时间 T，当须淘汰一个页面时，选择现有页面中其 T 值最大的，即最近最久未使用的页面予以淘汰。
- **LFU （Least Frequently Used）页面置换算法（最少使用页面置换算法）** : 该置换算法选择在之前时期使用最少的页面作为淘汰页。

### 17.IO五种模型

**从线程或者进程的角度来看，阻塞就是因为当前执行的这个线程，暂时的失去了CPU的执行权，被挂起等待下一次线程的调度或者线程被唤醒。**

- **阻塞式IO（BIO）**

  **阻塞式IO，是在等待数据和数据拷贝阶段都阻塞的一种IO模型，用户发起IO请求，在等待数据和数据拷贝阶段，都会被阻塞，只有这两个阶段都完成了，才能去做下一阶段的事情。**

  由于BIO阻塞时间长，因此相对性能就会较低，所以现在用的相对也比较少了

- **非阻塞式IO（NIO）**

  非阻塞IO，可以看作是半阻塞IO，因为他在第一阶段数据准备阶段不阻塞，第二阶段数据拷贝阶段阻塞，当用户发出IO请求的时候，**会有一个线程去询问内核数据准备好了吗**，一直问一直问，在这期间，用户主进程可以去干其他的事，等数据准备好了，到了第二阶段，这个时候，用户线程就要执行拷贝数据，这个时候是阻塞的。这种方式的缺点就是反复的轮训去询问内核数据好了没，是很消耗CPU资源的。

- **IO多路复用**

  IO请求都通过一个selector来管理，用户进程的IO请求就不直接发给内核处理程序了，而是注册到这个selector上面，由selector来告诉内核需要哪些数据，然后定时的去查询内核程序，我这个selector上需要的数据，有哪些准备好了，然后再由selector告诉那些准备好了的用户线程，让该用户线程去拷贝数据。**在非阻塞IO中，不断地询问状态时通过用户线程去进行的，而在IO多路复用中，询问每个状态是内核在进行的**，在IO请求非常多的时候，这个效率要比用户线程轮询要高的多。

- **全异步IO（AIO）**

  用户进程发起了一个IO请求，接下来可以干其他的事了，不需要等内核准备好，也不需要执行数据拷贝，数据异步拷贝到用户空间之后，用户进程直接拿来用就行了，这两个阶段都是由内核自动完成。完全不用用户线程操心这些事。

- **信号驱动IO**

  就是会有一个信号通知你数据已经准备好了，不用你一直去问。信号驱动IO，用户线程发出一个请求告诉内核我需要什么数据，数据准备好了你告诉我一声，然后内核就会记录下这个请求，内核准备好了之后会**主动通知**用户线程去执行拷贝数据，数据拷贝阶段是阻塞的，需要等数据拷贝完才能做其他的事。

  

### 18.IO多路复用

IO请求都通过一个selector来管理，用户进程的IO请求就不直接发给内核处理程序了，而是注册到这个selector上面，由selector来告诉内核需要哪些数据，然后定时的去查询内核程序，我这个selector上需要的数据，有哪些准备好了，然后再由selector告诉那些准备好了的用户线程，让该用户线程去拷贝数据。**在非阻塞IO中，不断地询问状态时通过用户线程去进行的，而在IO多路复用中，询问每个状态是内核在进行的**，在IO请求非常多的时候，这个效率要比用户线程轮询要高的多。

- Select
- Poll
- Epoll



### 19.僵尸进程、孤儿进程

1. 僵尸进程：一个父进程利用fork创建子进程，如果子进程退出，而父进程没有利用wait 或者 waitpid 来获取子进程的状态信息，那么子进程的状态描述符依然保存在系统中，这个子进程就是僵尸进程；

2. 孤儿进程：一个父进程退出， 而它的一个或几个子进程仍然还在运行，那么这些子进程就会变成孤儿进程，孤儿进程将被init进程（进程号为1）所收养，并由init进程对它们完成状态收集的工作。

3. 僵尸进程将会导致资源浪费，而孤儿进程则不会。



# **数据结构与算法**

### 1.10大排序算法

![10大排序算法](/Users/zhangyi/Desktop/Java复习图/10大排序算法.png)

### 2.快速排序

```java
/**
     * @param arr        待排序列
     * @param leftIndex  待排序列起始位置
     * @param rightIndex 待排序列结束位置
     */
    private static void quickSort(int[] arr, int leftIndex, int rightIndex) {
        if (leftIndex >= rightIndex) {
            return;
        }
        int left = leftIndex;
        int right = rightIndex;
        int key = arr[left];

        while (left < right) {
            while (right > left && arr[right] >= key) {
                right--;
            }
            arr[left] = arr[right];
            while (left < right && arr[left] <= key) {
                left++;
            }
            arr[right] = arr[left];
        }
        arr[left] = key;
        quickSort(arr, leftIndex, left - 1);
        quickSort(arr, right + 1, rightIndex);
    }
```



### 3.归并排序

### 4.堆排序

### 5.桶排序

### 6.希尔排序



#### [Trie 前缀树 LC208](https://leetcode-cn.com/problems/implement-trie-prefix-tree/) 

```
Trie（发音类似 "try"）或者说 前缀树 是一种树形数据结构，用于高效地存储和检索字符串数据集中的键。
这一数据结构有相当多的应用情景，例如自动补完和拼写检查。

请你实现 Trie 类：

Trie() 初始化前缀树对象。
void insert(String word) 向前缀树中插入字符串 word 。
boolean search(String word) 如果字符串 word 在前缀树中，返回 true（即，在检索之前已经插入）；否则，返回 false 。
boolean startsWith(String prefix) 如果之前已经插入的字符串 word 的前缀之一为 prefix ，返回 true ；否则，返回 false 。
```

```java
public class Trie {
    private Trie[] children;
    private boolean isEnd;

    public Trie() {
        children = new Trie[26];
        isEnd = false;
    }
		//插入单词
    public void insert(String word) {
        Trie node = this;
        for (int i = 0; i < word.length(); i++)
        {
            char ch = word.charAt(i);
            int index = ch - 'a';
            if (node.children[index] == null) node.children[index] = new Trie();
            node = node.children[index];
        }
        node.isEnd = true;
    }
		
  	//搜索单词
    public boolean search(String word) {
        Trie node = this;
        for (int i = 0; i < word.length(); i++)
        {
            node = node.children[word.charAt(i) - 'a'];
            if (node == null) return false;
        }
        return node.isEnd;
    }

  	//搜索前缀
    public boolean startsWith(String prefix) {
        Trie node = this;
        for (int i = 0; i < prefix.length(); i++)
        {
            node = node.children[prefix.charAt(i) - 'a'];
            if (node == null) return false;
        }
        return true;
    }
}
```



# Docker



# Kafka



# RabbitMQ



# ElasticSearch



# Dubbo



# Zookeeper