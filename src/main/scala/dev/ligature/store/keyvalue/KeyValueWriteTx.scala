/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package dev.ligature.store.keyvalue

import java.util.concurrent.atomic.AtomicBoolean

import cats.effect.IO
import dev.ligature._

import scala.util.{Failure, Success, Try}

final class KeyValueWriteTx(private val store: KeyValueStore) extends WriteTx {
  private val active = new AtomicBoolean(true)

  override def addStatement(collection: NamedEntity, statement: Statement): IO[Try[PersistedStatement]] = {
    ???
//    if (active.get()) {
//      val result = for {
//        col     <- createCollection(collection)
//        context <- newEntity(collection)
//        persistedStatement <- IO { PersistedStatement(collection, statement, context.get) }
//        statements <- IO { workingState.get()(collection).statements }
//        _ <- IO { statements.set(statements.get().incl(persistedStatement)) }
//      } yield Success(persistedStatement)
//      result
//    } else {
//      IO { Failure(new RuntimeException("Transaction is closed.")) }
//    }
  }

  override def cancel() {
    ???
//    if (active.get()) {
//      active.set(false)
//      lock.unlock()
//    } else {
//      throw new RuntimeException("Transaction is closed.")
//    }
  }

  override def commit(): Try[Unit] = {
    ???
//    if (active.get()) {
//      store.set(workingState.get())
//      lock.unlock()
//      active.set(false)
//      Success(())
//    } else {
//      Failure(new RuntimeException("Transaction is closed."))
//    }
  }

  override def createCollection(collection: NamedEntity): IO[Try[NamedEntity]] = {
    ???
//    if (active.get()) {
//      if (!workingState.get().contains(collection)) {
//        val oldState = workingState.get()
//        val newState = oldState.updated(collection,
//          CollectionValue(new AtomicReference(new HashSet[PersistedStatement]()),
//            new AtomicLong(0)))
//        val result = workingState.compareAndSet(oldState, newState)
//        IO { if (result) Success(collection) else Failure(new RuntimeException("Couldn't persist new collection.")) }
//      } else {
//        IO { Success(collection) } //collection exists
//      }
//    } else {
//      throw new RuntimeException("Transaction is closed.")
//    }
  }

  override def deleteCollection(collection: NamedEntity): IO[Try[NamedEntity]] = {
    ???
//    if (active.get()) {
//      val oldState = workingState.get()
//      val newState = oldState.removed(collection)
//      workingState.compareAndSet(oldState, newState)
//      IO { Success(collection) }
//    } else {
//      IO { Failure(new RuntimeException("Transaction is closed.")) }
//    }
  }

  override def isOpen(): Boolean = active.get()

  override def newEntity(collection: NamedEntity): IO[Try[AnonymousEntity]] = {
    ???
//    if (active.get()) {
//      for {
//        _ <- createCollection(collection)
//        newId <- IO { workingState.get()(collection).counter.incrementAndGet() }
//      } yield Success(AnonymousEntity(newId))
//    } else {
//      IO { Failure(new RuntimeException("Transaction is closed.")) }
//    }
  }

  override def removeEntity(collection: NamedEntity, entity: Entity): IO[Try[Entity]] = {
    ???
//    if (active.get()) {
//      if (workingState.get().contains(collection)) {
//        val subjectMatches = Common.matchStatementsImpl(workingState.get()(collection).statements.get(),
//          Some(entity))
//        val objectMatches = Common.matchStatementsImpl(workingState.get()(collection).statements.get(),
//          None, None, Some(entity))
//        val contextMatch = entity match {
//          case e: AnonymousEntity => Common.statementByContextImpl(workingState.get()(collection).statements.get(), e)
//          case _ => None
//        }
//        IO {
//          subjectMatches.foreach { p =>
//            workingState
//              .get()(collection)
//              .statements.set(workingState
//              .get()(collection).statements
//              .get().excl(p))
//          }
//          objectMatches.foreach { p =>
//            workingState
//              .get()(collection)
//              .statements.set(workingState
//              .get()(collection).statements
//              .get().excl(p))
//          }
//          if (contextMatch.nonEmpty) {
//            workingState
//              .get()(collection)
//              .statements.set(workingState
//              .get()(collection).statements
//              .get().excl(contextMatch.get))
//          }
//          Success(entity)
//        }
//      } else {
//        IO { Success(entity) }
//      }
//    } else {
//      IO { Failure(new RuntimeException("Transaction is closed.")) }
//    }
  }

  override def removePredicate(collection: NamedEntity, predicate: Predicate): IO[Try[Predicate]] = {
    ???
//    if (active.get()) {
//      if (workingState.get().contains(collection)) {
//        val persistedStatement = Common.matchStatementsImpl(workingState.get()(collection).statements.get(),
//          None,
//          Some(predicate))
//        IO {
//          persistedStatement.foreach { p =>
//            workingState
//              .get()(collection)
//              .statements.set(workingState
//              .get()(collection).statements
//              .get().excl(p))
//          }
//          Success(predicate)
//        }
//      } else {
//        IO { Success(predicate) }
//      }
//    } else {
//      IO { Failure(new RuntimeException("Transaction is closed.")) }
//    }
  }

  override def removeStatement(collection: NamedEntity, statement: Statement): IO[Try[Statement]] = {
    ???
//    if (active.get()) {
//      if (workingState.get().contains(collection)) {
//        val persistedStatement = Common.matchStatementsImpl(workingState.get()(collection).statements.get(),
//          Some(statement.subject),
//          Some(statement.predicate),
//          Some(statement.`object`))
//        IO {
//          persistedStatement.foreach { p =>
//            workingState
//              .get()(collection)
//              .statements.set(workingState
//              .get()(collection).statements
//              .get().excl(p))
//          }
//          Success(statement)
//        }
//      } else {
//        IO { Success(statement) }
//      }
//    } else {
//      IO { Failure(new RuntimeException("Transaction is closed.")) }
//    }
  }
}
