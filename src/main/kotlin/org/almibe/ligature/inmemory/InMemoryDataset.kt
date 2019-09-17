/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.almibe.ligature.inmemory

import org.almibe.ligature.*
import java.lang.RuntimeException
import java.util.*
import java.util.stream.Stream

internal class InMemoryDataset(private val name: String): Dataset {
    private data class Quad(val first: Int?, val second: Int, val third: Int, val fourth: Int?): Comparable<Quad> {
        override fun compareTo(other: Quad): Int {
            if (nullableIntCompare(first, other.first) != 0) {
                return nullableIntCompare(first, other.first)
            }
            if (second.compareTo(other.second) != 0) {
                return second.compareTo(other.second)
            }
            if (third.compareTo(other.third) != 0) {
                return third.compareTo(other.third)
            }
            return nullableIntCompare(fourth, other.fourth)
        }

        private fun nullableIntCompare(first: Int?, second: Int?): Int {
            return if (first == null && second == null) {
                0
            } else if (first == null) {
                -1
            } else if (second == null) {
                1
            } else {
                first.compareTo(second)
            }
        }
    }

    private var id = Int.MIN_VALUE                        // #cnt
    private val attributeId = mutableMapOf<Attribute, Int>()   // #aid
    private val idAttribute = mutableMapOf<Int, Attribute>()   // #ida
    private val literalId = mutableMapOf<Literal, Int>()  // #lid
    private val idLiteral = mutableMapOf<Int, Literal>()  // #idl
    private val eavc = TreeSet<Quad>()                    // #eavc
//    private val evac = TreeSet<Quad>()                    // #evac
//    private val avec = TreeSet<Quad>()                    // #avec
//    private val aevc = TreeSet<Quad>()                    // #aevc
//    private val veac = TreeSet<Quad>()                    // #veac
//    private val vaec = TreeSet<Quad>()                    // #vaec
//    private val ceav = TreeSet<Quad>()                    // #ceav

    @Synchronized override fun getDatasetName(): String {
        return name
    }

    @Synchronized override fun newNode(): Node {
        id++
        return Node((id).toString())
    }

    @Synchronized override fun addStatements(statements: Stream<Statement>) {
        statements.forEach {
            addStatement(it)
        }
    }

    private fun addStatement(statement: Statement) {
        val entityId = getAndCheckNodeId(statement.entity)
        val attributeId = getOrCreateAttributeId(statement.attribute)
        val valueId = getOrCreateValueId(statement.value)
        val contextId = getAndCheckNodeId(statement.context)

        eavc.add(Quad(entityId, attributeId, valueId, contextId))
    }

    private fun getAndCheckNodeId(node: Node?): Int? {
        if (node == null) {
            return null
        }
        val id = node.id.toInt()
        if (id > this.id) {
            throw RuntimeException("Invalid node id $id.")
        }
        return id
    }

    private fun getOrCreateAttributeId(attribute: Attribute): Int {
        return if (attributeId.containsKey(attribute)) {
            attributeId[attribute]!!
        } else {
            id++
            attributeId[attribute] = id
            idAttribute[id] = attribute
            id
        }
    }

    private fun getOrCreateValueId(value: Value): Int {
        return when (value) {
            is Node -> getAndCheckNodeId(value)!!
            is Literal -> getOrCreateLiteralId(value)
        }
    }

    private fun getOrCreateLiteralId(literal: Literal): Int {
        return if (literalId.containsKey(literal)) {
            literalId[literal]!!
        } else {
            id++
            literalId[literal] = id
            idLiteral[id] = literal
            id
        }
    }

    @Synchronized override fun matchAll(entity: Node?,
                                        attribute: Attribute?,
                                        value: Value?,
                                        context: Node?): Stream<Statement> {
        val entityId = getAndCheckNodeId(entity)
        val attributeId = attributeId[attribute]
        val valueId = getValueIdOrNull(value)
        val contextId = getAndCheckNodeId(context)

        return eavc.stream().filter {
            TODO()
        }.map {
            TODO()
        }
    }

    @Synchronized override fun removeStatements(statements: Stream<Statement>) {
        statements.forEach {
            removeStatement(it)
        }
    }

    private fun removeStatement(statement: Statement) {
        val entityId = getAndCheckNodeId(statement.entity)
        val attributeId = attributeId[statement.attribute]
        val valueId = getValueIdOrNull(statement.value)
        val contextId = getAndCheckNodeId(statement.context)

        if (entityId != null && attributeId != null && valueId != null) {
            eavc.remove(Quad(entityId, attributeId, valueId, contextId))
            checkAndCleanUpAttribute(attributeId)
            if (statement.value is Literal) {
                checkAndCleanUpLiteral(valueId)
            }
        }
    }

    private fun getValueIdOrNull(value: Value?): Int? {
        return when (value) {
            null -> null
            is Node -> getAndCheckNodeId(value)
            is Literal -> literalId[value]
        }
    }

    private fun checkAndCleanUpAttribute(attributeId: Int) {
        val result = eavc.firstOrNull { it.second == attributeId }
        if (result == null) {
            val attribute = idAttribute.remove(attributeId)
            this.attributeId.remove(attribute)
        }
    }

    private fun checkAndCleanUpLiteral(literalId: Int) {
        val result = eavc.firstOrNull { it.third == literalId }
        if (result == null) {
            val literal = idLiteral.remove(literalId)
            this.literalId.remove(literal)
        }
    }
}
