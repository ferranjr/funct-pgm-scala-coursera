package patmat

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import patmat.Huffman._

@RunWith(classOf[JUnitRunner])
class HuffmanSuite extends FunSuite {
  trait TestTrees {
    val t1 = Fork(Leaf('a',2), Leaf('b',3), List('a','b'), 5)
    val t2 = Fork(Fork(Leaf('a',2), Leaf('b',3), List('a','b'), 5), Leaf('d',4), List('a','b','d'), 9)
  }

  test("weight of a larger tree") {
    new TestTrees {
      assert(weight(t1) === 5)
    }
  }

  test("chars of a larger tree") {
    new TestTrees {
      assert(chars(t2) === List('a','b','d'))
    }
  }
  
  test("string2chars(\"hello, world\")") {
    assert(string2Chars("hello, world") === List('h', 'e', 'l', 'l', 'o', ',', ' ', 'w', 'o', 'r', 'l', 'd'))
  }

  test("makeOrderedLeafList for some frequency table") {
    assert(makeOrderedLeafList(List(('t', 2), ('e', 1), ('x', 3))) === List(Leaf('e',1), Leaf('t',2), Leaf('x',3)))
  }

  test("combine of some leaf list") {
    val leaflist = List(Leaf('e', 1), Leaf('t', 2), Leaf('x', 4))
    assert(combine(leaflist) === List(Fork(Leaf('e',1),Leaf('t',2),List('e', 't'),3), Leaf('x',4)))
  }
  
  test("decode and encode a very short text should be identity") {
    new TestTrees {
      println(encode(t1)("ab".toList))
      assert(decode(t1, encode(t1)("ab".toList)) === "ab".toList)
    }
  }
  
  test("createCodeTree.test.small ") {
    val text = "CodeTree"
    val tree: CodeTree = createCodeTree(text.toList)
    assertEncode(tree, text, 20)
  }

  def assertEncode(tree: CodeTree, text: String, bitCount:Int) {
    println("--------------------")
    println("Text: " + text)
    println("OptCodeTree: " + tree)

    println("Simple encode:")
    val enc1 = encode(tree)(text.toList)
    println("Bits: " + enc1)
    assert(decode(tree, enc1) === text.toList)

    println("Quick encode:")
    val enc2 = quickEncode(tree)(text.toList)
    println("Bits: " + enc2)
    assert(decode(tree, enc2) === text.toList)
    
    println("text decoded")
    println(decode(tree, enc1))
    
    assert(enc1 === enc2)
    assert(enc1.length === bitCount)
  }

}
