import scala.collection.immutable
val a = 10
println(a)

val l = List.empty[Int]

def checkList[A](list: List[A]):List[A]={
  list match{
    case x:: tail => tail
    case _ => List()
    //case Nil => List()
  }
}

checkList(List(1,3,2))
checkList(List.empty[String])

val list = List(List(1))
list.foldLeft(list){(x,y) =>
  val lastLine = x.reverse.head
  List(1 :: (lastLine.zip(lastLine.tail).map{p =>
    p._1 + p._2
  }).:+(1))
}

case class Test(id:Int, name:String)
val info = (1,"Anil")

Test(info._1,info._2)


def drawTriangles(n: Int):List[String]= {
  val x = 62
  val y = 31
  val canvas: List[String] = (0 to y).map{ yAxis =>
    (0 to x).map{ xAxis =>
        '_'
    }.mkString
  }.toList
  def drawInnerTri(num:Int,itr:Int,acc:List[String]):List[String] ={
    val tri = acc.zipWithIndex.map{ case(_,yAxis) =>
      (0 to x).map{ xAxis =>
        if(Math.abs((x/2)-xAxis)-yAxis <= 0){
          '1'
        }else {
          '_'
        }
      }.mkString
    }
    drawInvertTri(num, itr, tri)
  }

  def drawInvertTri(num:Int, itr:Int,acc:List[String]):List[String]={
    val s = acc.zipWithIndex.map{ case(line, yAxis) =>
      if(yAxis > y/2){
        line.zipWithIndex.map{ line =>
          val (ch,indx) = line
          //println(Math.abs((x/2)-(yAxis+indx)))
          //yAxis-indx
          //x/2-indx
          if(x-yAxis-indx >= 0 && yAxis-indx <=0){
            '_'
          }else{
            ch
          }
        }.mkString
      }else{
        line
      }
    }
    s
  }
  drawInnerTri(n,n,canvas)
}

drawTriangles(5).map{line => println(line)}