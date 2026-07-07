import { ApolloServer } from '@apollo/server';
import { startStandaloneServer } from '@apollo/server/standalone';
import { buildSubgraphSchema } from '@apollo/subgraph';
import gql from 'graphql-tag';
import { listBookings } from "./grpcClient.js";

const typeDefs = gql`
  type Booking @key(fields: "id") {
    id: ID!
    userId: String!
    hotelId: String!
    promoCode: String
    discountPercent: Float
    hotel: Hotel
  }
  
  extend type Hotel @key(fields: "id") {
    id: ID! @external
  }
  
  type Query {
    bookingsByUser(userId: String!): [Booking]
  }

`;

// const MOCK_BOOKINGS = [
//   { id: 'b1', userId: 'u1', hotelId: 'h1', discountPercent: 20, promoCode: 'SUMMER' },
//   { id: 'b2', userId: 'u2', hotelId: 'h2', discountPercent: 10, promoCode: 'WINTER' },
// ]

// async function fetchBookingsFromSource(userId) {
//   return MOCK_BOOKINGS.filter((b) => b.userId === userId);
// }

const resolvers = {
  Query: {
    bookingsByUser: async (_, { userId }, { req }) => {
      const requesterId = req.headers['userid'];

      if (!requesterId) {
        return [];
      }

      if (requesterId !== userId) {
        return [];
      }

      return listBookings(userId);
    },
  },
  Booking: {
    hotel: (booking) => ({ __typename: 'Hotel', id: booking.hotelId}),
  },
};


const server = new ApolloServer({
  schema: buildSubgraphSchema([{ typeDefs, resolvers }]),
});

startStandaloneServer(server, {
  listen: { port: 4001 },
  context: async ({ req }) => ({ req }),
}).then(() => {
  console.log('✅ Booking subgraph ready at http://localhost:4001/');
});
