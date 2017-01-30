package org.alfresco.decision.tree.model.test;

import org.alfresco.decision.tree.model.api.ConditionalNode;
import org.alfresco.decision.tree.model.api.Node;
import org.alfresco.decision.tree.model.api.Path;
import org.alfresco.decision.tree.model.api.Tree;
import org.alfresco.decision.tree.model.api.fluent.TreeFluent;
import org.alfresco.decision.tree.model.impl.ConditionalNodeImpl;
import org.alfresco.decision.tree.model.impl.EndNodeImpl;
import org.alfresco.decision.tree.model.impl.PathImpl;
import org.alfresco.decision.tree.model.impl.TreeImpl;
import org.junit.Test;

import static org.alfresco.decision.tree.model.api.Path.Operator.EQUALS;
import static org.alfresco.decision.tree.model.api.Path.Operator.GREATER_THAN;
import static org.alfresco.decision.tree.model.api.Path.Operator.LESS_THAN;
import static org.junit.Assert.*;

/**
 * Created by msalatino on 30/01/2017.
 */
public class DecisionTreeAPITest {

    @Test
    public void simplDecisionTreeAPITest(){
        Tree t = new TreeImpl("id", "my person tree", "my model content",  Person.class);
        ConditionalNode ageNode = new ConditionalNodeImpl("n0", "age");
        Path lt30Path = new PathImpl(LESS_THAN, "30");
        ConditionalNode cityNode = new ConditionalNodeImpl("n1", "city");
        lt30Path.setNodeTo(cityNode);
        Node endNodeDoesntApply = new EndNodeImpl("end0", "Doesn't Apply");

        Path mendozaPath = new PathImpl(EQUALS, "Mendoza");
        mendozaPath.setNodeTo(endNodeDoesntApply);
        Path londonPath = new PathImpl(EQUALS, "London");

        ConditionalNode marriedNode = new ConditionalNodeImpl("n2", "married");
        londonPath.setNodeTo(marriedNode);
        cityNode.addPath(mendozaPath);
        cityNode.addPath(londonPath);
        ageNode.addPath(lt30Path);
        Path gt30Path = new PathImpl(GREATER_THAN, "30");
        ageNode.addPath(gt30Path);
        Node endNodeTooOld = new EndNodeImpl("end1", "Too Old");
        gt30Path.setNodeTo(endNodeTooOld);
        Path marriedPath = new PathImpl(EQUALS, "true");
        Path notMarriedPath = new PathImpl(EQUALS, "false");
        Node endNodeSendAd2 = new EndNodeImpl("end2", "Send Ad 2");
        marriedPath.setNodeTo(endNodeSendAd2);
        Node endNodeSendAd1 = new EndNodeImpl("end3", "Send Ad 1");

        notMarriedPath.setNodeTo(endNodeSendAd1);
        marriedNode.addPath(marriedPath);
        marriedNode.addPath(notMarriedPath);

        t.setRootNode(ageNode);


    }

    @Test
    public void simplDecisionTreeFluentTest(){
        Tree t = new TreeFluent().newTree( "my person tree", Person.class)
                .condition("age")
                    .path(LESS_THAN, "30")
                        .condition("city")
                            .path(EQUALS, "Mendoza").end("Doesn't Apply")
                            .path(EQUALS, "London")
                                .condition("married")
                                    .path(EQUALS, "true").end("Send Ad 2")
                                    .path(EQUALS, "false").end("Send Ad 1")
                                .endCondition()
                            .endCondition()
                    .path(GREATER_THAN, "30").end("Too Old")
                .endCondition()
                .build();



    }
}