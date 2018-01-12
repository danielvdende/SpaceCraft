val str = "ABCD"

def rotateWord(str:String):String={
  def rotateOne(subStr:String, acc:List[String],count:Int):List[String]={
    if(count <= 1){
      acc ++ List(str)
    }else{
      val rotatedWord = subStr.tail + subStr.head
      rotateOne(rotatedWord, acc ++ List(rotatedWord),count -1)
    }
  }
  rotateOne(str, List.empty[String],str.length).mkString(" ")
}
rotateWord("ABC")
println(rotateWord("ABCDE"))

val l = List("ABC","ABCDE")

l.map(str => rotateWord(str))

def removeDuplicates(str:String):String={
  def removeOne(subStr:String,acc:String):String ={
    if(subStr.length < 1){
      acc
    }else if(acc.contains(subStr.head)){
        removeOne(subStr.tail, acc)
    }else{
        removeOne(subStr.tail,acc+ subStr.head)
    }
  }
  removeOne(str, "")
}
removeDuplicates("AAcbAbdffc")


def stringMingle(str1:String, str2: String): String = {
  str1.zip(str2).toList.map{ s => s"${s._1}${s._2}"}.mkString
}

stringMingle("abcde", "pqrst")

def stringOPermute(str:String):String ={
  str.grouped(2).map(ss => ss.reverse).mkString
}

stringOPermute("ABCDEF")

def pascelTriangle(num:Int):List[List[Int]] = {
  def nextPasceline(list: List[Int]) :List[Int] = {
    1 :: list.zip(list.tail).map{ pair =>
      pair._1 + pair._2
    } ::: List(1)
  }
  def inner(row:Int,acc:List[List[Int]]):List[List[Int]]={
    if(row < 2){
      acc
    }else{
      val nextLine = nextPasceline(acc.reverse.head)
      inner(row-1,acc :+ nextLine)
    }
  }
  inner(num,List(List(1)))
}

pascelTriangle(6).map{line => println(line.mkString(" "))}

/*
val list = List(1)
1 :: list.zip(list.tail).map{ pair =>
  pair._1 + pair._2
} ::: List(1)*/