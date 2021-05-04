package com.javacourse.stack;

import java.util.stream.IntStream;

import com.antkorwin.commonutils.gc.LeakDetector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IntegerStackTest {

	@Nested
	class ComplexTests {

		@Test
		@DisplayName("Комплексный тест работы со стеком")
		void complexTest() {
			// Arrange
			IntegerStack stack = new IntegerStack();
			stack.push(1);
			stack.push(2);
			stack.push(3);
			// Act & assert
			assertThat(stack.peek()).isEqualTo(3);
			assertThat(stack.pop()).isEqualTo(3);
			assertThat(stack.peek()).isEqualTo(2);
			assertThat(stack.pop()).isEqualTo(2);
			assertThat(stack.peek()).isEqualTo(1);
			assertThat(stack.pop()).isEqualTo(1);
			// Empty stack assert
			assertThrows(RuntimeException.class, stack::pop);
		}
	}

	@Nested
	class PeekTests {

		@Test
		@DisplayName("вызов метода peek всегда возвращает верхний элемент")
		void peekTheSameElemWhenNothingGetFromStack() {
			IntegerStack stack = new IntegerStack();
			stack.push(1);
			stack.push(2);
			stack.push(3);
			assertThat(stack.peek()).isEqualTo(3);
			assertThat(stack.peek()).isEqualTo(3);
			assertThat(stack.peek()).isEqualTo(3);
		}

		@Test
		@DisplayName("вызов метода peek не влияет на структуру стека")
		void peekIsNotChangeTheStackContent() {
			IntegerStack stack = new IntegerStack();
			stack.push(1);
			stack.push(2);
			stack.push(3);
			stack.peek();
			stack.peek();
			stack.peek();
			assertThat(stack.pop()).isEqualTo(3);
			assertThat(stack.pop()).isEqualTo(2);
			assertThat(stack.pop()).isEqualTo(1);
		}

		@Test
		@DisplayName("вызов метода peek на пустом стеке приводит к исключению")
		void peekFromEmptyStack() {
			IntegerStack stack = new IntegerStack();
			RuntimeException exc = assertThrows(RuntimeException.class, stack::peek);
			assertThat(exc).hasMessage("Empty Stack Exception");
		}
	}

	@Nested
	class PopTests {

		@Test
		@DisplayName("вызов метода pop всегда возвращает верхний элемент")
		void popFromEmptyStack() {
			IntegerStack stack = new IntegerStack();
			RuntimeException exc = assertThrows(RuntimeException.class, stack::pop);
			assertThat(exc).hasMessage("Empty Stack Exception");
		}

		@Test
		@DisplayName("вызов метода pop на стеке из одного элемента")
		void singlePushAndPop() {
			IntegerStack stack = new IntegerStack();
			stack.push(123);
			assertThat(stack.pop()).isEqualTo(123);
			assertThrows(RuntimeException.class, stack::pop);
		}
	}

	@Nested
	class PushTests {

		@Test
		@DisplayName("push позволяет добавить null-value элемент в стек")
		void pushNullValue() {
			IntegerStack stack = new IntegerStack();
			stack.push(1);
			stack.push(2);
			stack.push(null);
			stack.push(3);
			assertThat(stack.pop()).isEqualTo(3);
			assertThat(stack.pop()).isEqualTo(null);
			assertThat(stack.pop()).isEqualTo(2);
			assertThat(stack.pop()).isEqualTo(1);
			assertThrows(RuntimeException.class, stack::pop);
		}


		@Test
		@DisplayName("push добавляет элементы в стек даже если такие уже в нем были")
		void pushEqualItems() {
			IntegerStack stack = new IntegerStack();
			stack.push(1);
			stack.push(1);
			stack.push(1);
			assertThat(stack.pop()).isEqualTo(1);
			assertThat(stack.pop()).isEqualTo(1);
			assertThat(stack.pop()).isEqualTo(1);
			assertThrows(RuntimeException.class, stack::pop);
		}

		@Test
		@DisplayName("push каждый раз добавляет элемент в самый верх стека")
		void pushOrdering() {
			IntegerStack stack = new IntegerStack();
			stack.push(1);
			stack.push(2);
			stack.push(3);
			assertThat(stack.pop()).isEqualTo(3);
			assertThat(stack.pop()).isEqualTo(2);
			assertThat(stack.pop()).isEqualTo(1);
		}

		@Test
		@DisplayName("push после того как из стека достали все элементы")
		void pushAfterPopAll() {
			IntegerStack stack = new IntegerStack();
			stack.push(1);
			stack.push(2);
			stack.push(3);
			stack.pop();
			stack.pop();
			stack.pop();
			stack.push(4);
			assertThat(stack.pop()).isEqualTo(4);
			assertThrows(RuntimeException.class, stack::pop);
		}
	}

	@Test
	@DisplayName("стек не хранит ссылок на элементы, которые были извлечены из стека")
	void memoryLeak() {
		IntegerStack stack = new IntegerStack();
		Integer markerObject = new Integer(123456789);
		LeakDetector leakDetector = new LeakDetector(markerObject);
		stack.push(markerObject);
		stack.pop();
		markerObject = null;
		leakDetector.assertMemoryLeaksNotExist();
	}

	@Test
	@DisplayName("добавляем и извлекаем из стека сотню элементов")
	void hundredItems() {
		int limit = 100;
		IntegerStack stack = new IntegerStack();
		// Arrange
		IntStream.range(0, limit)
		         .forEach(stack::push);
		// Act & Assert
		IntStream.range(0, limit)
		         .forEach(i -> assertThat(stack.pop()).isEqualTo(limit - i - 1));
	}
}