package dev.javarush.feeder.event;

public interface EventPublisher<E> {
  void publish(E event);
}
